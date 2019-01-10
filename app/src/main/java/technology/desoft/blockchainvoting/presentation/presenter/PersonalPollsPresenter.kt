package technology.desoft.blockchainvoting.presentation.presenter

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.ActivePollDetailsNavigation
import technology.desoft.blockchainvoting.navigation.navigations.CompletedPollDetailsNavigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PersonalPollsView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import java.util.*

@InjectViewState
class PersonalPollsPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val pollsRepository: PollRepository,
    private val userProvider: UserTokenProvider,
    private val userRepository: UserRepository
    ) : MvpPresenter<PersonalPollsView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showPersonalPolls()
    }

    private fun showPersonalPolls() {
        viewState.loading()
        val job = launch(Dispatchers.IO) {
            val polls = pollsRepository.getPolls().await()
            val users = userRepository.getUsers().await()
            val userId = userProvider.getUserId()
            if (users != null && polls != null) {
                val author = users.find { it.id == userId } ?: return@launch
                val filteredPolls = polls.filter { it.userId == userId }
                val pollViews = filteredPolls.map { PollAndAuthor(it, author) }
                launch(Dispatchers.Main) { viewState.showPersonalPolls(pollViews.toMutableList()) }.start()
            }
        }
        jobs.add(job)
        job.start()
    }

    fun showDetails(pollAndAuthor: PollAndAuthor, view: View) {
        if (pollAndAuthor.poll.endsAt > Calendar.getInstance().time)
            router.postNavigation(ActivePollDetailsNavigation(pollAndAuthor, view))
        else
            router.postNavigation(CompletedPollDetailsNavigation(pollAndAuthor, view))
    }

    fun removePoll(id: Long) {
        val job = pollsRepository.removePoll(id)
        jobs.add(job)
        job.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}