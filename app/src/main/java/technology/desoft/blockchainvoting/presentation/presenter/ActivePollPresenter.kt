package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.vote.AddVoteResult
import technology.desoft.blockchainvoting.model.network.vote.Vote
import technology.desoft.blockchainvoting.model.network.vote.VoteRepository
import technology.desoft.blockchainvoting.presentation.view.ActivePollView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor

@InjectViewState
class ActivePollPresenter(
    private val coroutineScope: CoroutineScope,
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

        refreshing = true
        viewState.showDetails(pollAndAuthor)
        showOptions()
    }

    private fun showOptions() {
        val job = launch(Dispatchers.IO) {
            options = pollRepository.getOptions(pollAndAuthor.poll.id).await() ?: return@launch
            launch(Dispatchers.Main) {
                viewState.showOptions(options)
                if (userAlreadyVoted)
                    viewState.lockOptions()
            }.start()
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
            if (tryVote(selected).userAlreadyVoted)
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

    private suspend fun tryVote(selected: Int): AddVoteResult {
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