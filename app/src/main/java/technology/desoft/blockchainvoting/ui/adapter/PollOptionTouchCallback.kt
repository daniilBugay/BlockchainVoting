package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.item_add_option.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.presenter.AddPollPresenter
import technology.desoft.blockchainvoting.ui.changeElevation

class PollOptionTouchCallback(private val adapter: AddOptionAdapter, private val presenter: AddPollPresenter)
    : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = true
    override fun isLongPressDragEnabled() = true
    private var focusCallBack: (() -> Unit)? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder == null) return
        val card = (viewHolder as AddOptionAdapter.ViewHolder).itemView.addOptionItemCard ?: return
        val defaultElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_default)
        val activeElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_active)
        card.changeElevation(defaultElevation, activeElevation)
        focusCallBack?.invoke()
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val card = (viewHolder as AddOptionAdapter.ViewHolder).itemView.addOptionItemCard ?: return
        val defaultElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_active)
        val activeElevation = viewHolder.itemView.context.resources.getDimension(R.dimen.elevation_default)
        card.changeElevation(defaultElevation, activeElevation)
        focusCallBack?.invoke()
        super.clearView(recyclerView, viewHolder)
    }

    fun setFocusCallBack(onFocus: () -> Unit){
        focusCallBack = onFocus
    }

    override fun getMovementFlags(recycler: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        val moveFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(moveFlags, swipeFlags)
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        if (holder !is AddOptionAdapter.ViewHolder || holder.isHeader) return
        presenter.removeOption(holder.adapterPosition)
        adapter.removeOption(holder.adapterPosition)
    }

    override fun onMove(recycler: RecyclerView, from: RecyclerView.ViewHolder, to: RecyclerView.ViewHolder): Boolean {
        if (from !is AddOptionAdapter.ViewHolder || to !is AddOptionAdapter.ViewHolder || from.isHeader || to.isHeader)
            return false
        presenter.moveOption(from.adapterPosition, to.adapterPosition)
        adapter.moveOption(from.adapterPosition, to.adapterPosition)
        return true
    }
}