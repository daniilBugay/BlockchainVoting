package technology.desoft.blockchainvoting.ui.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.text.format.DateFormat
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_poll_details.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.ActivePollDetailsPresenter
import technology.desoft.blockchainvoting.presentation.view.ActivePollDetailsView
import technology.desoft.blockchainvoting.presentation.view.PollView
import java.util.*

class ActivePollDetailsFragment : MvpAppCompatFragment(), ActivePollDetailsView {
    companion object {
        private const val POLL_KEY = "poll"
        private const val TRANSITION_NAME_KEY = "transition name"

        fun withPoll(poll: PollView): ActivePollDetailsFragment {
            val result = ActivePollDetailsFragment()
            val json = Gson().toJson(poll)
            val bundle = Bundle()
            bundle.putString(POLL_KEY, json)
            bundle.putString(TRANSITION_NAME_KEY, "pollCard${poll.poll.id}")
            result.arguments = bundle
            return result
        }
    }

    @InjectPresenter
    lateinit var activePollDetailPresenter: ActivePollDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): ActivePollDetailsPresenter {
        val json = arguments?.getString(POLL_KEY)
            ?: throw IllegalStateException("You must create fragment using withPoll companion function")
        val poll = Gson().fromJson<PollView>(json, PollView::class.java)
        return ActivePollDetailsPresenter(poll)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_poll_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.pollDetailsCard.transitionName = arguments?.getString(TRANSITION_NAME_KEY)
        val animation = AnimationUtils.loadAnimation(context, R.anim.move_up)
        view.pollDescriptionCard.startAnimation(animation)
        view.pollDetailsMakeChoiceText.setOnClickListener {
            val behavior = BottomSheetBehavior.from(view.pollDetailsOptionsLayout)
            behavior.state = if (behavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        }
        this.exitTransition = Slide(Gravity.BOTTOM)
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
            completedText.visibility = if (pollView.poll.endsAt < Calendar.getInstance().timeInMillis) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}