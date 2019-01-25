package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface AddPollView: MvpView {
    @StateStrategyType(AddToEndStrategy::class)
    fun addOption(optionContent: String)
    fun error(message: String)
    fun hideButton()
    fun showButton()
    fun finishAdding()
}