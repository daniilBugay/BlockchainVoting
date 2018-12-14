package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.*
import technology.desoft.blockchainvoting.presentation.view.ActivePollDetailsView
import technology.desoft.blockchainvoting.presentation.view.PollView

@InjectViewState
class ActivePollDetailsPresenter(
    private val coroutineScope: CoroutineScope,
    private val pollRepository: PollRepository,
    private val voteRepository: VoteRepository,
    private val pollView: PollView
) : MvpPresenter<ActivePollDetailsView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    private var currentSelected: Int? = null
    private lateinit var options: List<PollOption>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollView)
        val job = launch(Dispatchers.IO) {
            options = pollRepository.getOptions(pollView.poll.id).await()
            launch(Dispatchers.Main) {
                viewState.showOptions(options)
            }.start()
        }
        jobs.add(job)
        job.start()
    }

    fun onSelectOption(position: Int) {
        currentSelected = position
        viewState.setSelectedOption(position)
    }

    fun vote() {
        val selected = this.currentSelected
        if (selected != null) {
            voteRepository.addVote(options[selected].id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}