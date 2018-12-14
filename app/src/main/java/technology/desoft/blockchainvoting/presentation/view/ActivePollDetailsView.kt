package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import technology.desoft.blockchainvoting.model.PollOption

interface ActivePollDetailsView: MvpView {
    fun showDetails(pollView: PollView)
    fun showOptions(options: List<PollOption>)
    fun setSelectedOption(position: Int)
}