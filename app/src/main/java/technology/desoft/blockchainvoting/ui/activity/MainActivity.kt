package technology.desoft.blockchainvoting.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
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
import technology.desoft.blockchainvoting.ui.fragment.ActivePollDetailsFragment
import technology.desoft.blockchainvoting.ui.fragment.AllPollsFragment

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

    override fun showLoginScreen() {

    }

    override fun showAllPolls() {
        changeFragment(AllPollsFragment()) {}
    }

    override fun showActivePollDetails(poll: PollView, itemView: View) {
        val fragment = ActivePollDetailsFragment.withPoll(poll)
        changeFragment(fragment) {
            addToBackStack(null)
            val transitionSet = TransitionSet()
            transitionSet.duration = 350L
            transitionSet.addTransition(
                TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move)
            )
            fragment.sharedElementEnterTransition = transitionSet
            fragment.enterTransition = Fade()
            val card = itemView.pollCard
            addSharedElement(card, card.transitionName)
        }
    }

    override fun showCompletedPollDetails(poll: PollView, itemView: View) {

    }

    override fun onBackPressed() {
        val isProcessed = supportFragmentManager.fragments
            .filterIsInstance<OnBackListener>()
            .firstOrNull { it.onBack() }

        if (isProcessed == null) super.onBackPressed()
    }
}
