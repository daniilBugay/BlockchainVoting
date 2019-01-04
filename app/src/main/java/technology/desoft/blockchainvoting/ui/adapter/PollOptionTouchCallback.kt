package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import technology.desoft.blockchainvoting.presentation.presenter.AddPollPresenter

class PollOptionTouchCallback(private val adapter: AddOptionAdapter, private val presenter: AddPollPresenter)
    : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = true
    override fun isLongPressDragEnabled() = true

    override fun getMovementFlags(recycler: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        val moveFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(moveFlags, swipeFlags)
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        presenter.removeOption(holder.adapterPosition)
        adapter.removeOption(holder.adapterPosition)
    }

    override fun onMove(recycler: RecyclerView, from: RecyclerView.ViewHolder, to: RecyclerView.ViewHolder): Boolean {
        presenter.moveOption(from.adapterPosition, to.adapterPosition)
        adapter.moveOption(from.adapterPosition, to.adapterPosition)
        return true
    }
}