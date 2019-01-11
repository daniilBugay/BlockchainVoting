package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import technology.desoft.blockchainvoting.model.network.vote.Vote
import technology.desoft.blockchainvoting.model.network.vote.VoteRepository
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.presentation.view.ActivePollView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor

@InjectViewState
class ActivePollPresenter(
    private val coroutineScope: CoroutineScope,
    private val pollRepository: PollRepository,
    private val voteRepository: VoteRepository,
    private val userTokenProvider: UserTokenProvider,
    private val pollAndAuthor: PollAndAuthor
) : MvpPresenter<ActivePollView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    private var currentSelected: Int? = null
    private lateinit var options: List<PollOption>
    private var lastSelectedOption: Vote? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollAndAuthor)
        val job = launch(Dispatchers.IO) {
            options = pollRepository.getOptions(pollAndAuthor.poll.id).await() ?: return@launch
            launch(Dispatchers.Main) {
                lastSelectedOption = getCurrentVote().await()
                viewState.showOptions(options)
                currentSelected?.let {
                    viewState.setSelectedOption(it)
                    viewState.lockOptions()
                    viewState.lockButton()
                }
            }.start()
        }
        jobs.add(job)
        job.start()
    }

    private suspend fun getCurrentVote() = async {
        val userId = userTokenProvider.userId
        val vote = options.flatMap { voteRepository.getVotes(it.id).await() }
            .firstOrNull { it.userId == userId }
            ?: return@async null
        currentSelected = options.indexOfFirst { it.id == vote.pollOptionId }
        vote
    }

    fun onSelectOption(position: Int) {
        if (lastSelectedOption != null) return
        currentSelected = position
        viewState.setSelectedOption(position)
        viewState.unlockButton()
    }

    fun vote() {
        val selected = this.currentSelected
        if (selected != null) {
            voteRepository.addVote(options[selected].id)
            viewState.lockButton()
            viewState.lockOptions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}