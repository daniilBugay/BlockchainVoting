package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import technology.desoft.blockchainvoting.presentation.view.ActivePollDetailsView
import technology.desoft.blockchainvoting.presentation.view.PollView

@InjectViewState
class ActivePollDetailsPresenter(private val pollView: PollView): MvpPresenter<ActivePollDetailsView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollView)
    }
}