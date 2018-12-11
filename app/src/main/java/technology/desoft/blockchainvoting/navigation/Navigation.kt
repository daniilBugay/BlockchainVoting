package technology.desoft.blockchainvoting.navigation

import com.arellomobile.mvp.MvpView

interface Navigation<T: MvpView> {
    fun apply(view: T)
}