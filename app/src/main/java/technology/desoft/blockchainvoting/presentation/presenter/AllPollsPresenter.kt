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
import technology.desoft.blockchainvoting.navigation.navigations.*
import technology.desoft.blockchainvoting.presentation.view.AllPollsView
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import java.util.*

@InjectViewState
class AllPollsPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val pollsRepository: PollRepository,
    private val userRepository: UserRepository,
    private val userTokenProvider: UserTokenProvider
) : MvpPresenter<AllPollsView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()
    private var polls: List<PollAndAuthor>? = null
    private var searchRequest: String? = null

    fun refresh(){
        showPolls(searchRequest)
    }

    private fun showPolls(searchText: String? = null) {
        val job = launch(Dispatchers.IO) {
            val loadedPolls = pollsRepository.getPolls().await()
            val users = userRepository.getUsers().await()
            if (users != null && loadedPolls != null) {
                val pollViews = loadedPolls.mapNotNull {
                    val author = it.findAuthor(users)
                    if (author == null)
                        null
                    else
                        PollAndAuthor(it, author)
                }
                polls = pollViews
                search(searchText)
            }
        }
        jobs.add(job)
        job.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

    fun showDetails(pollAndAuthor: PollAndAuthor, view: View) {
        if (pollAndAuthor.poll.endsAt > Calendar.getInstance().time)
            router.postNavigation(ActivePollDetailsNavigation(pollAndAuthor, view))
        else
            router.postNavigation(CompletedPollDetailsNavigation(pollAndAuthor, view))
    }

    fun onAddPoll() {
        router.postNavigation(AddPollNavigation())
    }

    fun onPersonalShow() {
        router.postNavigation(PersonalPollsNavigation())
    }

    fun search(searchText: String?) {
        searchRequest = searchText
        val currentPolls = this.polls ?: return
        if (searchText.isNullOrEmpty()){
            launch(Dispatchers.Main) { viewState.showPolls(currentPolls) }.start()
        } else {
            val searchPolls = currentPolls
                .filter { searchText in it.poll.theme}
                .sortedBy { it.poll.theme.indexOf(searchText) }
            launch(Dispatchers.Main) { viewState.showPolls(searchPolls) }.start()
        }
    }

    fun logOut() {
        userTokenProvider.clear()
        router.postNavigation(LogOutNavigation())
    }
}