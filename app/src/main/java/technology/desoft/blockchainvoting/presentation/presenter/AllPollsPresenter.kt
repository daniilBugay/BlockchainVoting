package technology.desoft.blockchainvoting.presentation.presenter

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.Poll
import technology.desoft.blockchainvoting.model.PollRepository
import technology.desoft.blockchainvoting.model.UserRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.PollDetailsNavigation
import technology.desoft.blockchainvoting.presentation.view.AllPollsView
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollView

@InjectViewState
class AllPollsPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val pollsRepository: PollRepository,
    private val userRepository: UserRepository
) : MvpPresenter<AllPollsView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showPolls()
    }

    fun showPolls() {
        viewState.loading()
        val job = launch(Dispatchers.IO) {
            val polls = pollsRepository.getPolls().await()
            val users = userRepository.getUsers().await()
            if (users != null && polls != null) {
                val pollViews = polls.mapNotNull {
                    val author = it.findAuthor(users)
                    if (author == null)
                        null
                    else
                        PollView(it, author)
                }
                launch(Dispatchers.Main) { viewState.showPolls(pollViews) }.start()
            }
        }
        jobs.add(job)
        job.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

    fun showDetails(pollView: PollView, view: View) {
        router.postNavigation(PollDetailsNavigation(pollView, view))
    }
}