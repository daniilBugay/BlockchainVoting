package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.item_add_option.view.*
import kotlinx.android.synthetic.main.item_poll.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.PersonalPollsPresenter
import technology.desoft.blockchainvoting.ui.changeElevation

class PollTouchCallback(private val adapter: PollsAdapter, private val presenter: PersonalPollsPresenter) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = true
    override fun isLongPressDragEnabled() = false

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder == null) return
        val card = (viewHolder as PollsAdapter.ViewHolder).itemView.pollCard
        val defaultElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_default)
        val activeElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_active)
        card.changeElevation(defaultElevation, activeElevation)
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val card = (viewHolder as PollsAdapter.ViewHolder).itemView.pollCard
        val defaultElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_active)
        val activeElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_default)
        card.changeElevation(defaultElevation, activeElevation)
        super.clearView(recyclerView, viewHolder)
    }

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