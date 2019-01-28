package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.vote.AddVoteResult
import technology.desoft.blockchainvoting.model.network.vote.Vote
import technology.desoft.blockchainvoting.model.network.vote.VoteRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.CompletedPollDetailsNavigation
import technology.desoft.blockchainvoting.presentation.view.ActivePollView
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import java.util.*

@InjectViewState
class ActivePollPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val pollRepository: PollRepository,
    private val voteRepository: VoteRepository,
    private val pollAndAuthor: PollAndAuthor,
    private val resources: Resources
) : MvpPresenter<ActivePollView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    private var currentSelected: Int? = null
    private lateinit var options: List<PollOption>
    private var lastSelectedOption: Vote? = null
    private var refreshing = false
    private var userAlreadyVoted = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollAndAuthor)
        showOptions()
    }

    fun refresh() {
        if (refreshing) return

        val date = Calendar.getInstance().time
        if (pollAndAuthor.poll.endsAt > date){
            router.postNavigation(CompletedPollDetailsNavigation(pollAndAuthor))
            return
        }
        refreshing = true
        viewState.showDetails(pollAndAuthor)
        showOptions()
    }

    private fun showOptions() {
        val job = launch(Dispatchers.IO) {
            options = pollRepository.getOptions(pollAndAuthor.poll.id).await() ?: return@launch
            withContext(Dispatchers.Main) {
                viewState.showOptions(options)
                if (userAlreadyVoted)
                    viewState.lockOptions()
            }
        }
        jobs.add(job)
        job.invokeOnCompletion { refreshing = false }
        job.start()
    }

    fun onSelectOption(position: Int) {
        if (lastSelectedOption != null) return
        currentSelected = position
        viewState.setSelectedOption(position)
        viewState.unlockButton()
    }

    fun vote() {
        val selected = this.currentSelected ?: return

        val job = launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                viewState.lockButton()
                viewState.lockOptions()
            }
            if (tryVote(selected)?.userAlreadyVoted == true)
                withContext(Dispatchers.Main) {
                    userAlreadyVoted = true
                    userAlreadyVotedError()
                }
            else
                showOptions()
        }
        jobs.add(job)
        job.start()
    }

    private suspend fun tryVote(selected: Int): AddVoteResult? {
        return voteRepository.addVote(pollAndAuthor.poll.id, options[selected].id).await()
    }

    private fun userAlreadyVotedError() {
        viewState.showError(resources.getString(R.string.vote_error))
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}