package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
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
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import technology.desoft.blockchainvoting.ui.adapter.PollsAdapter

class AllPollsFragment : MvpAppCompatFragment(), AllPollsView {

    @InjectPresenter
    lateinit var allPollsPresenter: AllPollsPresenter

    @ProvidePresenter
    fun providePresenter(): AllPollsPresenter {
        val app = activity?.application as App
        return AllPollsPresenter(
            GlobalScope,
            app.mainRouter,
            app.pollRepository,
            app.userRepository,
            app.userProvider
        )
    }

    fun isUserAdmin() = (activity?.application as App).userProvider.token.isAdmin

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.pollsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.pollsAddButton.setOnClickListener {
            allPollsPresenter.onAddPoll()
        }
        setHasOptionsMenu(true)
        view.allPollsRefresh.setOnRefreshListener {
            view.pollsAddButton?.hide()
            allPollsPresenter.refresh()
        }
        view.pollsRecycler?.adapter = PollsAdapter(mutableListOf()) { pollAndAuthor, transitionView ->
            allPollsPresenter.showDetails(pollAndAuthor, transitionView)
        }
        setSearchView(view)
    }

    private fun setSearchView(view: View) {
        view.allPollsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(text: String?): Boolean {
                allPollsPresenter.search(text)
                return true
            }
            override fun onQueryTextSubmit(text: String?) = false
        })
    }

    override fun onResume() {
        super.onResume()
        allPollsPresenter.refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (isUserAdmin())
            inflater?.inflate(R.menu.menu_main, menu)
        else
            inflater?.inflate(R.menu.menu_logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.personal_menu_item -> allPollsPresenter.onPersonalShow()
            R.id.logout_menu_item -> allPollsPresenter.logOut()
            else -> return false
        }
        return true
    }

    override fun showError(message: String) {
        view?.allPollsRefresh?.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showPolls(polls: List<PollAndAuthor>) {
        view?.allPollsRefresh?.isRefreshing = false
        if (isUserAdmin())
            Handler().postDelayed({ view?.pollsAddButton?.show() }, 450)

        (view?.pollsRecycler?.adapter as PollsAdapter).setPolls(polls)
    }
}