package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface AllPollsView: MvpView {
    fun showPolls(polls: List<PollAndAuthor>)
    @StateStrategyType(SkipStrategy::class)
    fun showError(message: String)
}