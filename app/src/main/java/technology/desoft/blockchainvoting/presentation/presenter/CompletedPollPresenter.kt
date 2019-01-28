package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.presentation.view.CompletedPollView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor

@InjectViewState
class CompletedPollPresenter(
    private val coroutineScope: CoroutineScope,
    private val pollRepository: PollRepository,
    private val pollAndAuthor: PollAndAuthor
) : MvpPresenter<CompletedPollView>(), CoroutineScope by coroutineScope {

    private val jobs = mutableListOf<Job>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollAndAuthor)
        val job = launch(Dispatchers.IO){

            val options = pollRepository.getOptions(pollAndAuthor.poll.id).await()
            withContext(Dispatchers.Main) {
                options?.let { viewState.showPollResult(it) }
            }
        }
        job.start()
        jobs.add(job)
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}