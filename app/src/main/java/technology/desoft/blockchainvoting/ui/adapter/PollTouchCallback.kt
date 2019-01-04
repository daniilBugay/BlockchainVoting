package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import technology.desoft.blockchainvoting.presentation.presenter.PersonalPollsPresenter

class PollTouchCallback(private val adapter: PollsAdapter, private val presenter: PersonalPollsPresenter) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = true
    override fun isLongPressDragEnabled() = false

    override fun getMovementFlags(recycler: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        val moveFlags = 0
        return makeMovementFlags(moveFlags, swipeFlags)
    }

    override fun onMove(recycler: RecyclerView, from: RecyclerView.ViewHolder, to: RecyclerView.ViewHolder) = false

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        val pollView = adapter.getPollByPosition(holder.adapterPosition)
        presenter.removePoll(pollView.poll.id)
        adapter.removePoll(holder.adapterPosition)
    }

}