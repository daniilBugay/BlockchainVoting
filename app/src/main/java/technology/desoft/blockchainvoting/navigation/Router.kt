package technology.desoft.blockchainvoting.navigation

import com.arellomobile.mvp.MvpView

interface Router<T: MvpView> {
    fun setView(view: T)
    fun postNavigation(navigation: Navigation<T>)
}