package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface AddPollView: MvpView {
    fun addOption(optionContent: String)
    fun error(message: String)
}