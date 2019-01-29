package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_poll_option.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.ui.PercentDrawable

class PollOptionsAdapter(
    private val pollOptions: List<PollOption>,
    private val onSelected: (Int) -> Unit
): RecyclerView.Adapter<PollOptionsAdapter.ViewHolder>() {

    private var isLocked = false
    private var currentSelected: Int? = null

    private companion object {
        const val ELEMENT = 0
        const val FOOTER = 1
    }

    override fun getItemCount() = pollOptions.size

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_poll_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    fun setLocked(locked: Boolean){
        isLocked = locked
        notifyDataSetChanged()
    }

    fun setSelected(position: Int){
        currentSelected = position
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val option = pollOptions[position]
            val percent = option.interest?.toInt() ?: 0
            itemView.background = PercentDrawable(itemView.context, percent)
            itemView.pollOptionContent.text = option.content
            itemView.pollOptionRadio.isChecked = currentSelected == position
            itemView.pollOptionRadio.text = itemView.context.getString(R.string.percent, percent)
            if (!isLocked) {
                itemView.pollOptionRadio.setOnClickListener {
                    onSelected(position)
                }
            } else {
                itemView.pollOptionRadio.isEnabled = false
            }
        }
    }
}