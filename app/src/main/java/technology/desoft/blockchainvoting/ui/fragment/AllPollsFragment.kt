package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_all_polls.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.AllPollsPresenter
import technology.desoft.blockchainvoting.presentation.view.AllPollsView
import technology.desoft.blockchainvoting.presentation.view.PollView
import technology.desoft.blockchainvoting.ui.adapter.PollsAdapter

class AllPollsFragment : MvpAppCompatFragment(), AllPollsView {

    @InjectPresenter
    lateinit var allPollsPresenter: AllPollsPresenter

    @ProvidePresenter
    fun providePresenter(): AllPollsPresenter {
        val app = activity?.application as App
        return AllPollsPresenter(GlobalScope, app.mainRouter, app.pollRepository, app.userRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.pollsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun showError(message: String) {
        view?.pollsProgressBar?.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showPolls(polls: List<PollView>) {
        view?.pollsProgressBar?.visibility = View.GONE
        Handler().postDelayed({view?.pollsAddButton?.show()}, 350)
        view?.pollsRecycler?.adapter = PollsAdapter(polls) { pollView, view ->
            allPollsPresenter.showDetails(pollView, view)
        }
    }

    override fun loading() {
        view?.pollsProgressBar?.visibility = View.VISIBLE
    }
}