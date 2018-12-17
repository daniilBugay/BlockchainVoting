package technology.desoft.blockchainvoting.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import android.widget.ProgressBar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.item_poll.view.*
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.MainPresenter
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollView
import technology.desoft.blockchainvoting.ui.OnBackListener
import technology.desoft.blockchainvoting.ui.fragment.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        val app = application as App
        return MainPresenter(app.mainRouter)
    }

    private inline fun changeFragment(fragment: Fragment, body: FragmentTransaction.() -> Unit) {
        supportFragmentManager.beginTransaction()
            .apply(body)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showSignInScreen() {
        changeFragment(SignInFragment()) {}
    }

    override fun showSignUpScreen() {
        changeFragment(SignUpFragment()) {}
    }

    override fun showAllPolls() {
        val fragment = AllPollsFragment()
        changeFragment(fragment) {
            val currentFragment = supportFragmentManager.fragments.firstOrNull()
            val transitionSet = TransitionSet()
            transitionSet.addTransition(
                TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move)
            )
            transitionSet.duration = 350
            fragment.sharedElementEnterTransition = transitionSet
            if (currentFragment != null){
                val progressBar = currentFragment.view?.findViewById<ProgressBar>(R.id.signInProgressBar)
                if (progressBar != null) {
                    addSharedElement(progressBar, progressBar.transitionName)
                }
            }
        }
    }

    private fun showDetails(detailsFragment: Fragment, itemView: View) {
        changeFragment(detailsFragment) {
            addToBackStack(null)
            val transitionSet = TransitionSet()
            transitionSet.duration = 350L
            transitionSet.addTransition(
                TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move)
            )
            detailsFragment.sharedElementEnterTransition = transitionSet
            detailsFragment.enterTransition = Fade()
            val card = itemView.pollCard
            addSharedElement(card, card.transitionName)
        }
    }

    override fun showActivePollDetails(poll: PollView, itemView: View) {
        showDetails(ActivePollFragment.withPoll(poll), itemView)
    }

    override fun showCompletedPollDetails(poll: PollView, itemView: View) {
        showDetails(CompletedPollFragment.withPoll(poll), itemView)
    }

    override fun onBackPressed() {
        val isProcessed = supportFragmentManager.fragments
            .filterIsInstance<OnBackListener>()
            .firstOrNull { it.onBack() }

        if (isProcessed == null) super.onBackPressed()
    }
}
