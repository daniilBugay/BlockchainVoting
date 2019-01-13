package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_poll.*
import kotlinx.android.synthetic.main.fragment_personal_polls.*
import kotlinx.android.synthetic.main.fragment_personal_polls.view.*
import kotlinx.android.synthetic.main.item_poll_result.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.PersonalPollsPresenter
import technology.desoft.blockchainvoting.presentation.view.PersonalPollsView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor
import technology.desoft.blockchainvoting.ui.adapter.PollTouchCallback
import technology.desoft.blockchainvoting.ui.adapter.PollsAdapter

class PersonalPollsFragment : MvpAppCompatFragment(), PersonalPollsView {

    @InjectPresenter
    lateinit var personalPollsPresenter: PersonalPollsPresenter

    @ProvidePresenter
    fun providePresenter(): PersonalPollsPresenter {
        val app = activity?.application as App
        return PersonalPollsPresenter(
            GlobalScope,
            app.mainRouter,
            app.pollRepository,
            app.userProvider,
            app.userRepository
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_personal_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.personalPollsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.personalPollsRefresh.setOnRefreshListener { personalPollsPresenter.refresh() }

        val adapter = PollsAdapter(mutableListOf()) { pollView, itemView ->
            personalPollsPresenter.showDetails(pollView, itemView)
        }
        val touchHelper = ItemTouchHelper(PollTouchCallback(adapter, personalPollsPresenter))
        view.personalPollsRecycler.adapter = adapter
        touchHelper.attachToRecyclerView(view.personalPollsRecycler)
        setHasOptionsMenu(true)
        Handler().postDelayed({view.personalBackButton.show()}, 350)
        personalBackButton.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        personalPollsPresenter.refresh()
    }

    override fun showError(message: String) {
        view?.personalPollsRefresh?.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showPersonalPolls(personalPolls: MutableList<PollAndAuthor>) {
        val view = this.view ?: return

        (view.personalPollsRecycler.adapter as PollsAdapter).setPolls(personalPolls)
        view.personalPollsRefresh?.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout_menu_item -> personalPollsPresenter.logOut()
            else -> return false
        }
        return true
    }
}