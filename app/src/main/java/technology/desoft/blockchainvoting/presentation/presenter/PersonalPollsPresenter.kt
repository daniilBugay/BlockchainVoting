package technology.desoft.blockchainvoting.presentation.presenter

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import technology.desoft.blockchainvoting.model.PollRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.navigations.ActivePollDetailsNavigation
import technology.desoft.blockchainvoting.navigation.navigations.CompletedPollDetailsNavigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PersonalPollsView
import technology.desoft.blockchainvoting.presentation.view.PollView
import java.util.*

@InjectViewState
class PersonalPollsPresenter(
    private val coroutineScope: CoroutineScope,
    private val router: Router<MainView>,
    private val pollsRepository: PollRepository
) : MvpPresenter<PersonalPollsView>(), CoroutineScope by coroutineScope {
    private val jobs: MutableList<Job> = mutableListOf()

    fun showDetails(pollView: PollView, view: View) {
        if (pollView.poll.endsAt > Calendar.getInstance().timeInMillis)
            router.postNavigation(ActivePollDetailsNavigation(pollView, view))
        else
            router.postNavigation(CompletedPollDetailsNavigation(pollView, view))
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}