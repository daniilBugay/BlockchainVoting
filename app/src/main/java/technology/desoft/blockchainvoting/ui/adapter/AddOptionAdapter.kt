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
    override fun getItemCount() = optionContents.size

    private var onDragStart: ((ViewHolder) -> Unit)? = null

    fun setOnDragStartListener(onDragStart: (ViewHolder) -> Unit){
        this.onDragStart = onDragStart
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_add_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun addOption(optionContext: String){
        optionContents.add(0, optionContext)
        notifyItemInserted(0)
    }

    fun removeOption(position: Int){
        if (position == -1) return
        optionContents.removeAt(position)
        notifyItemRemoved(position)
    }

    fun moveOption(from: Int, to: Int){
        val temp = optionContents[from]
        optionContents[from] = optionContents[to]
        optionContents[to] = temp

        notifyItemMoved(from, to)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val optionContent = optionContents[position]
            itemView.addOptionContentText.text = optionContent
            itemView.addOptionDragButton.setOnTouchListener { v, event ->
                    onDragStart?.invoke(this)
                true
            }
        }
    }
}