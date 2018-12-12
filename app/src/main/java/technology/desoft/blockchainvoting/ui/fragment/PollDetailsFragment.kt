package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_poll_details.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.PollDetailsPresenter
import technology.desoft.blockchainvoting.presentation.view.PollDetailsView
import technology.desoft.blockchainvoting.presentation.view.PollView

class PollDetailsFragment : MvpAppCompatFragment(), PollDetailsView {
    companion object {
        private const val POLL_KEY = "poll"
        private const val TRANSITION_NAME_KEY = "transition name"

        fun withPoll(poll: PollView): PollDetailsFragment {
            val result = PollDetailsFragment()
            val json = Gson().toJson(poll)
            val bundle = Bundle()
            bundle.putString(POLL_KEY, json)
            bundle.putString(TRANSITION_NAME_KEY, "pollCard${poll.poll.id}")
            result.arguments = bundle
            return result
        }
    }

    @InjectPresenter
    lateinit var pollDetailPresenter: PollDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): PollDetailsPresenter {
        val json = arguments?.getString(POLL_KEY)
            ?: throw IllegalStateException("You must create fragment using withPoll companion function")
        val poll = Gson().fromJson<PollView>(json, PollView::class.java)
        return PollDetailsPresenter(poll)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_poll_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.pollDetailsCard.transitionName = arguments?.getString(TRANSITION_NAME_KEY)
    }

    override fun showDetails(pollView: PollView) {
        view?.apply {
            pollDetailsTheme.text = pollView.poll.theme
            pollDetailsDescription.text = pollView.poll.description
            pollDetailsAuthor.text = pollView.author.email
            val formatter = DateFormat.getDateFormat(context)
            val fromDate = formatter.format(pollView.poll.createdAt)
            val toDate = formatter.format(pollView.poll.endsAt)
            pollDetailsDate.text = resources.getString(R.string.date_format, fromDate, toDate)
        }
    }
}