package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_all_polls.view.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.PersonalPollsPresenter
import technology.desoft.blockchainvoting.presentation.view.PersonalPollsView
import technology.desoft.blockchainvoting.presentation.view.PollView

class PersonalPollsFragment: MvpAppCompatFragment(), PersonalPollsView {
    @InjectPresenter
    lateinit var personalPollsPresenter: PersonalPollsPresenter

    @ProvidePresenter
    fun providePresenter(): PersonalPollsPresenter{
        val app = activity?.application as App
        return PersonalPollsPresenter(GlobalScope, app.mainRouter, app.pollRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_personal_polls, container, false)
    }

    override fun loading() {
        view?.signInProgressBar?.visibility = View.VISIBLE
    }

    override fun showPersonalPolls(personalPolls: List<PollView>) {
        view?.pollsProgressBar?.visibility = View.GONE
    }

    override fun deletePersonalPoll(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}