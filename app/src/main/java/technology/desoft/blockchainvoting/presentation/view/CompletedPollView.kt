package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import technology.desoft.blockchainvoting.model.network.polls.PollOption

@StateStrategyType(AddToEndSingleStrategy::class)
interface CompletedPollView: MvpView {
    fun showDetails(pollAndAuthor: PollAndAuthor)
    fun showPollResult(options: List<PollOption>)
}