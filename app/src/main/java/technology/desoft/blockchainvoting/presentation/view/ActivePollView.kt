package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import technology.desoft.blockchainvoting.model.network.polls.PollOption

@StateStrategyType(AddToEndSingleStrategy::class)
interface ActivePollView: MvpView {
    fun showDetails(pollAndAuthor: PollAndAuthor)
    fun showOptions(options: List<PollOption>)
    fun setSelectedOption(position: Int)
    fun lockButton()
    fun unlockButton()
    fun lockOptions()
    @StateStrategyType(SkipStrategy::class)
    fun showError(message: String)
}