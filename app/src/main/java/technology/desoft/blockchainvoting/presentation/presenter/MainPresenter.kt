package technology.desoft.blockchainvoting.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.presentation.view.MainView

@InjectViewState
class MainPresenter(private val router: Router<MainView>): MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.setView(viewState)
        viewState.showPersonalPolls()
    }

}