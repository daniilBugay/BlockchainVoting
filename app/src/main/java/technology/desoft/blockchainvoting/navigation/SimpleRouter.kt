package technology.desoft.blockchainvoting.navigation

import com.arellomobile.mvp.MvpView

class SimpleRouter<T: MvpView>: Router<T> {
    private var view: T? = null

    override fun setView(view: T) {
        this.view = view
    }

    override fun postNavigation(navigation: Navigation<T>) {
        view?.let { navigation.apply(it) }
    }
}