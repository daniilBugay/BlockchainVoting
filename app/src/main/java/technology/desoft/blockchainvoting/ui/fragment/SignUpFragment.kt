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
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.SignUpPresenter
import technology.desoft.blockchainvoting.presentation.view.SignView

class SignUpFragment: MvpAppCompatFragment(), SignView {

    @InjectPresenter
    lateinit var signUpPresenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter(): SignUpPresenter{
        val app = activity?.application as App
        return SignUpPresenter(GlobalScope, app.mainRouter, app.userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.signUpButton.setOnClickListener{
            val email = view.emailSignUpEditText.text.toString()
            val password = view.passwordSignUpEditText.text.toString()
            val passwordConfirm = view.passwordConfirmEditText.text.toString()
            signUpPresenter.registration(email, password, passwordConfirm)
        }
        view.signUpTransitionInButton.setOnClickListener {
            signUpPresenter.transitionToSignIn()
        }
    }

    override fun loading() {
        view?.signUpProgressBar?.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT) }
        snackBar?.view?.setBackgroundColor(
            ContextCompat.getColor(context!!, R.color.colorRed)
        )

        val snackBarText = snackBar?.view?.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.gravity = Gravity.CENTER_HORIZONTAL
        snackBarText.setTextColor(Color.WHITE)
        snackBarText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
        view?.signUpProgressBar?.visibility = View.GONE
    }

    override fun showSuccess(message: String) {
        val snackBar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT) }
        snackBar?.view?.setBackgroundColor(
            ContextCompat.getColor(context!!, R.color.colorGreen)
        )

        val snackBarText = snackBar?.view?.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.gravity = Gravity.CENTER_HORIZONTAL
        snackBarText.setTextColor(Color.WHITE)
        snackBarText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        snackBar.show()
        view?.signUpProgressBar?.visibility = View.GONE
    }
}