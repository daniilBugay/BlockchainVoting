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
    fun showActivePollDetails(poll: PollView, itemView: View)
    fun showCompletedPollDetails(poll: PollView, itemView: View)
}