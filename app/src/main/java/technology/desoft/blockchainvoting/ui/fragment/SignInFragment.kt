package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.SignInPresenter
import technology.desoft.blockchainvoting.presentation.view.SignView

class SignInFragment : MvpAppCompatFragment(), SignView {

    @InjectPresenter
    lateinit var signInPresenter: SignInPresenter

    @ProvidePresenter
    fun providePresenter(): SignInPresenter {
        val app = activity?.application as App
        return SignInPresenter(GlobalScope, app.mainRouter, app.userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.signInButton.setOnClickListener {
            val email = view.emailEditText.text.toString()
            val password = view.passwordEditText.text.toString()
            signInPresenter.login(email, password)
        }
    }

    override fun loading() {
        view?.signInProgressBar?.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        view?.signInProgressBar?.visibility = View.GONE
    }

    override fun showSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        view?.signInProgressBar?.visibility = View.GONE
    }

}