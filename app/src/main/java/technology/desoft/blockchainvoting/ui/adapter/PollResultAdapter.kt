package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_poll_result.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.PollOption
import technology.desoft.blockchainvoting.ui.PercentDrawable

class PollResultAdapter(private val pollResults: List<PollOption>): RecyclerView.Adapter<PollResultAdapter.ViewHolder>() {

    override fun getItemCount() = pollResults.size

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_poll_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val optionResult = pollResults[position]
            val percent = optionResult.interest?.toInt() ?: 0
            itemView.background = PercentDrawable(itemView.context, percent)
            itemView.apply {
                pollOptionContent.text = optionResult.content
                pollOptionResult.text = resources.getString(R.string.percent, percent)
            }
        }
    }
}