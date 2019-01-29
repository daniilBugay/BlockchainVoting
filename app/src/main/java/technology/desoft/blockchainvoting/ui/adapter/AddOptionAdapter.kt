package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_add_option.view.*
import technology.desoft.blockchainvoting.R

class AddOptionAdapter(
    private val optionContents: MutableList<String>
): RecyclerView.Adapter<AddOptionAdapter.ViewHolder>() {
    private companion object {
        const val ELEMENT = 0
        const val FOOTER = 1
    }

    override fun getItemCount() = optionContents.size + 1

    private var onDragStart: ((ViewHolder) -> Unit)? = null

    fun setOnDragStartListener(onDragStart: (ViewHolder) -> Unit){
        this.onDragStart = onDragStart
    }

    override fun getItemViewType(position: Int) = when(position){
        itemCount - 1 -> FOOTER
        else -> ELEMENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (type == ELEMENT) {
            val view = inflater.inflate(R.layout.item_add_option, parent, false)
            ViewHolder(view, false)
        } else {
            ViewHolder(inflater.inflate(R.layout.item_empty_footer, parent, false), true)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun addOption(optionContext: String){
        optionContents.add(0, optionContext)
        notifyItemInserted(0)
    }

    fun removeOption(position: Int){
        if (position == -1 || getItemViewType(position) == FOOTER) return
        optionContents.removeAt(position)
        notifyItemRemoved(position)
    }

    fun moveOption(from: Int, to: Int){
        if (getItemViewType(from) == FOOTER || getItemViewType(to) == FOOTER) return
        val temp = optionContents[from]
        optionContents[from] = optionContents[to]
        optionContents[to] = temp

        notifyItemMoved(from, to)
    }

    fun clear() {
        optionContents.clear()
    }

    inner class ViewHolder(itemView: View, val isHeader: Boolean): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            if (isHeader) return
            val optionContent = optionContents[position]
            itemView.addOptionContentText.text = optionContent
            itemView.addOptionDragButton.setOnTouchListener { _, _->
                    onDragStart?.invoke(this)
                true
            }
        }
    }
}