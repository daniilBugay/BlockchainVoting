package technology.desoft.blockchainvoting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        return SignInPresenter(
            GlobalScope,
            app.mainRouter,
            app.userRepository,
            app.pollRepository,
            app.voteRepository,
            app.userProvider,
            resources
        )
    }

    private fun showCustomSnackBar(message: String, backgroundColor: Int){
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT) }
        snackBar?.view?.setBackgroundColor(
            ContextCompat.getColor(context!!, backgroundColor)
        )

        val snackBarText = snackBar?.view?.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.gravity = Gravity.CENTER_HORIZONTAL
        snackBarText.setTextColor(Color.WHITE)
        snackBarText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.signInButton.setOnClickListener {
            val email = view.emailSignInEditText.text.toString()
            val password = view.passwordSignInEditText.text.toString()
            signInPresenter.login(email, password)
        }
        view.signInTransitionUpButton.setOnClickListener {
            signInPresenter.transitionToSignUp()
        }
    }

    override fun loading() {
        view?.signInProgressBar?.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        showCustomSnackBar(message, R.color.colorRed)
        view?.signInProgressBar?.visibility = View.GONE
    }

    override fun showSuccess(message: String) {
        showCustomSnackBar(message, R.color.colorAccent)
    }
}