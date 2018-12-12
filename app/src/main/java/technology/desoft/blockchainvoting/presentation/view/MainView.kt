package technology.desoft.blockchainvoting.presentation.view

import android.view.View
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface MainView: MvpView {
    fun showLoginScreen()
    fun showAllPolls()
    fun showPollDetails(poll: PollView, itemView: View)
}