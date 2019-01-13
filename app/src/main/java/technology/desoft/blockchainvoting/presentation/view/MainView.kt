package technology.desoft.blockchainvoting.presentation.view

import android.view.View
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface MainView: MvpView {
    fun showSignInScreen()
    fun showSignUpScreen()
    fun showAllPolls()
    fun showPersonalPolls()
    fun showActivePollDetails(poll: PollAndAuthor, itemView: View)
    fun showCompletedPollDetails(poll: PollAndAuthor, itemView: View)
    fun showAddScreen()
    fun logOut()
}