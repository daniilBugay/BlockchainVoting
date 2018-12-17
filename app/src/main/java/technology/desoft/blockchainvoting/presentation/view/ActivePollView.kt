package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import technology.desoft.blockchainvoting.model.PollOption

interface ActivePollView: MvpView {
    fun showDetails(pollView: PollView)
    fun showOptions(options: List<PollOption>)
    fun setSelectedOption(position: Int)
    fun lockButton()
    fun unlockButton()
    fun lockOptions()
}