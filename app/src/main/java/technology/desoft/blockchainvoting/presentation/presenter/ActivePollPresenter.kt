package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import retrofit2.HttpException
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollAndAuthor)
        showOptions()
    }

    fun refresh(){
        if (refreshing) return

        refreshing = true
        viewState.showDetails(pollAndAuthor)
        showOptions()
    }

    private fun showOptions(){
        val job = launch(Dispatchers.IO) {
            options = pollRepository.getOptions(pollAndAuthor.poll.id).await() ?: return@launch
            launch(Dispatchers.Main) {
                viewState.showOptions(options)
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
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            processError(throwable as Exception)
        }
        val job = launch(Dispatchers.IO + errorHandler) {
            try {
                //TODO("error")
                tryVote(selected)
            } catch (e: Exception){
                processError(e)
            }
        }
        jobs.add(job)
        job.start()
    }

    private suspend fun tryVote(selected: Int) {
        launch(Dispatchers.Main) {
            viewState.lockButton()
            viewState.lockOptions()
        }
        voteRepository.addVote(pollAndAuthor.poll.id, options[selected].id).join()
        showOptions()
    }

    private fun processError(exception: Exception) {
        fun showNetworkError() = viewState.showError(resources.getString(R.string.network_error))

        if (exception is HttpException) {
            when (exception.code()) {
                406 -> viewState.showError(resources.getString(R.string.vote_error))
                else -> showNetworkError()
            }
        } else {
            showNetworkError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}