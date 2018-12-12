package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import technology.desoft.blockchainvoting.presentation.view.PollDetailsView
import technology.desoft.blockchainvoting.presentation.view.PollView

@InjectViewState
class PollDetailsPresenter(private val pollView: PollView): MvpPresenter<PollDetailsView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showDetails(pollView)
    }
}