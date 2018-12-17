package technology.desoft.blockchainvoting.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_poll_details.view.*
import kotlinx.coroutines.GlobalScope
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.PollOption
import technology.desoft.blockchainvoting.presentation.presenter.ActivePollPresenter
import technology.desoft.blockchainvoting.presentation.view.ActivePollView
import technology.desoft.blockchainvoting.presentation.view.PollView
import technology.desoft.blockchainvoting.ui.OnBackListener
import technology.desoft.blockchainvoting.ui.adapter.PollOptionsAdapter
import java.util.*

class ActivePollFragment : MvpAppCompatFragment(), ActivePollView, OnBackListener {
    companion object {
        private const val POLL_KEY = "poll"
        private const val TRANSITION_NAME_KEY = "transition name"

        fun withPoll(poll: PollView): ActivePollFragment {
            val result = ActivePollFragment()
            val json = Gson().toJson(poll)
            val bundle = Bundle()
            bundle.putString(POLL_KEY, json)
            bundle.putString(TRANSITION_NAME_KEY, "pollCard${poll.poll.id}")
            result.arguments = bundle
            return result
        }
    }

    @InjectPresenter
    lateinit var activePollDetailPresenter: ActivePollPresenter

    @ProvidePresenter
    fun providePresenter(): ActivePollPresenter {
        val json = arguments?.getString(POLL_KEY)
            ?: throw IllegalStateException("You must create fragment using withPoll companion function")
        val poll = Gson().fromJson<PollView>(json, PollView::class.java)
        val app = activity?.application as App
        return ActivePollPresenter(GlobalScope, app.pollRepository, app.voteRepository, app.userProvider, poll)
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
        view.pollDetailsOptionsRecycler.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        view.pollDetailsVoteButton.setOnClickListener {
            activePollDetailPresenter.vote()
        }
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

    override fun showOptions(options: List<PollOption>) {
        view?.pollDetailsOptionsRecycler?.adapter = PollOptionsAdapter(options) {
            activePollDetailPresenter.onSelectOption(it)
        }
    }

    override fun setSelectedOption(position: Int) {
        val adapter = view?.pollDetailsOptionsRecycler?.adapter as PollOptionsAdapter?
        adapter?.setSelected(position)
    }

    override fun lockButton() {
        view?.pollDetailsVoteButton?.isEnabled = false
        view?.pollDetailsVoteButton?.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context!!, R.color.colorSignGray5)
        )
    }

    override fun unlockButton() {
        view?.pollDetailsVoteButton?.isEnabled = true
    }

    override fun onBack(): Boolean {
        val behavior = BottomSheetBehavior.from(view?.pollDetailsOptionsLayout)
        return if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        } else {
            false
        }
    }

    override fun lockOptions() {
        (view?.pollDetailsOptionsRecycler?.adapter as PollOptionsAdapter).setLocked(true)
    }
}