package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface SignView: MvpView {
    fun loading()
    fun showError(message: String)
    fun showSuccess(message: String)
}