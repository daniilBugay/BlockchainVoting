package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_poll.view.*
import kotlinx.android.synthetic.main.fragment_personal_polls.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.PersonalPollsPresenter
import technology.desoft.blockchainvoting.presentation.view.PersonalPollsView
import technology.desoft.blockchainvoting.presentation.view.PollView
import technology.desoft.blockchainvoting.ui.adapter.AddOptionAdapter
import technology.desoft.blockchainvoting.ui.adapter.PollOptionTouchCallback
import technology.desoft.blockchainvoting.ui.adapter.PollTouchCallback
import technology.desoft.blockchainvoting.ui.adapter.PollsAdapter

class PersonalPollsFragment: MvpAppCompatFragment(), PersonalPollsView {

    @InjectPresenter
    lateinit var personalPollsPresenter: PersonalPollsPresenter

    @ProvidePresenter
    fun providePresenter(): PersonalPollsPresenter{
        val app = activity?.application as App
        return PersonalPollsPresenter(GlobalScope, app.mainRouter, app.pollRepository, app.userProvider, app.userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_personal_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.personalPollsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun loading() {
        view?.personalPollsProgressBar?.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        view?.personalPollsProgressBar?.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showPersonalPolls(personalPolls: MutableList<PollView>) {
        val view = this.view ?: return
        val adapter = PollsAdapter(personalPolls) { pollView, itemView ->
            personalPollsPresenter.showDetails(pollView, itemView)
        }
        val touchHelper = ItemTouchHelper(PollTouchCallback(adapter, personalPollsPresenter))
        view.personalPollsRecycler.adapter = adapter
        touchHelper.attachToRecyclerView(view.personalPollsRecycler)
        view.personalPollsProgressBar?.visibility = View.GONE
    }
}