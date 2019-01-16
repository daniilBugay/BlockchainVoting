package technology.desoft.blockchainvoting.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.CardView
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
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import technology.desoft.blockchainvoting.ui.CircularAnimationProvider
import technology.desoft.blockchainvoting.ui.OnBackListener
import technology.desoft.blockchainvoting.ui.fragment.*
import technology.desoft.blockchainvoting.ui.notification.NotificationSender

class MainActivity : MvpAppCompatActivity(), MainView {

    private companion object {
        const val NOTIFICATION_PERIOD = 10_000L
    }

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        val app = application as App
        return MainPresenter(
            app.mainRouter,
            app.userProvider,
            app.userRepository,
            app.pollRepository,
            app.voteRepository
        )
    }

    private inline fun changeFragment(fragment: Fragment, body: FragmentTransaction.() -> Unit) {
        supportFragmentManager.beginTransaction()
            .apply(body)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private inline fun changeFragmentWithTransition(fragment: Fragment, body: FragmentTransaction.() -> Unit) {
        changeFragment(fragment) {
            val transitionSet = TransitionSet()
            transitionSet.duration = 350L
            transitionSet.addTransition(
                TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move)
            )
            fragment.sharedElementEnterTransition = transitionSet
            body()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            registerReceiver()
        }
    }

    private fun registerReceiver() {
        val intent = Intent(this, NotificationSender::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, NOTIFICATION_PERIOD, pendingIntent)
    }

    override fun showSignInScreen() {
        val fragment = SignInFragment()
        changeFragmentWithTransition(fragment) {
            val currentFragment = supportFragmentManager.fragments.firstOrNull()
            if (currentFragment != null) {
                val card: CardView? = currentFragment.view?.findViewById(R.id.signUpCard)
                val button: MaterialButton? = currentFragment.view?.findViewById<MaterialButton?>(R.id.signUpButton)
                if (card != null && button != null) {
                    addSharedElement(card, card.transitionName)
                    addSharedElement(button, button.transitionName)
                }
            }
        }
    }

    override fun showSignUpScreen() {
        val fragment = SignUpFragment()
        changeFragmentWithTransition(fragment) {
            val currentFragment = supportFragmentManager.fragments.firstOrNull()
            if (currentFragment != null) {
                val card: CardView? = currentFragment.view?.findViewById(R.id.signInCard)
                val button: MaterialButton? = currentFragment.view?.findViewById(R.id.signInButton)
                if (card != null && button != null) {
                    addSharedElement(card, card.transitionName)
                    addSharedElement(button, button.transitionName)
                }
            }
        }
    }

    override fun showAllPolls() {
        changeFragment(AllPollsFragment()) {}
    }

    override fun showPersonalPolls() {
        val fragment = PersonalPollsFragment()
        changeFragment(fragment) { addToBackStack(null) }
    }

    private fun showDetails(detailsFragment: Fragment, itemView: View) {
        changeFragmentWithTransition(detailsFragment) {
            addToBackStack(null)
            detailsFragment.enterTransition = Fade()
            val card = itemView.pollCard
            addSharedElement(card, card.transitionName)
        }
    }

    override fun showActivePollDetails(poll: PollAndAuthor, itemView: View) {
        showDetails(ActivePollFragment.withPoll(poll), itemView)
    }

    override fun showCompletedPollDetails(poll: PollAndAuthor, itemView: View) {
        showDetails(CompletedPollFragment.withPoll(poll), itemView)
    }

    override fun showAddScreen() {
        val currentFragment = supportFragmentManager.fragments.firstOrNull()
        if (currentFragment != null && currentFragment is AllPollsFragment) {
            val fab = currentFragment.view?.findViewById<FloatingActionButton?>(R.id.pollsAddButton)
            fab?.let {
                val x = it.x + it.width / 2
                val y = it.y + it.height / 2

                changeFragment(AddPollFragment.withCircularAnimation(x, y)) {
                    addToBackStack(null)
                }
            }
        } else {
            changeFragment(AddPollFragment()) {
                addToBackStack(null)
            }
        }

    }

    override fun onBackPressed() {
        val isProcessed = supportFragmentManager.fragments
            .filterIsInstance<OnBackListener>()
            .firstOrNull { it.onBack() }

        if (isProcessed == null) {
            val currentFragment = supportFragmentManager.fragments.firstOrNull()

            if (currentFragment != null && currentFragment is CircularAnimationProvider.Dismissible)
                currentFragment.dismiss { super.onBackPressed() }
            else
                super.onBackPressed()
        }
    }

    override fun logOut() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
