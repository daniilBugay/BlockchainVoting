package technology.desoft.blockchainvoting.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_poll.view.*
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.presentation.view.PollView
import java.util.*

class PollsAdapter(
    private val polls: List<PollView>,
    private val onClick: (PollView, View) -> Unit
): RecyclerView.Adapter<PollsAdapter.ViewHolder>() {

    override fun getItemCount() = polls.size

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_poll, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val pollView = polls[position]
            with(itemView){
                pollTheme.text = pollView.poll.theme
                pollAuthor.text = pollView.author.email
                val formatter = DateFormat.getDateFormat(itemView.context)
                val fromDate = formatter.format(pollView.poll.createdAt)
                val toDate = formatter.format(pollView.poll.endsAt)
                pollDate.text = resources.getString(R.string.date_format, fromDate, toDate)
                pollCard.transitionName = "pollCard${pollView.poll.id}"
                pollCard.completedText.visibility = if (pollView.poll.endsAt < Calendar.getInstance().timeInMillis){
                    View.VISIBLE
                } else {
                    View.GONE
                }
                setOnClickListener { onClick(pollView, itemView) }
            }
        }
    }
}