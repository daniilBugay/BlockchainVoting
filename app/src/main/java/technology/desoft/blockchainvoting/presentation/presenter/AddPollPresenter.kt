package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.CreatePollOptionView
import technology.desoft.blockchainvoting.model.network.polls.CreatePollView
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.presentation.view.AddPollView
import java.util.*

@InjectViewState
class AddPollPresenter(
    private val resources: Resources,
    private val pollRepository: PollRepository
) : MvpPresenter<AddPollView>() {
    private val options: MutableList<String> = mutableListOf()
    private val jobs = mutableListOf<Job>()
    private var endsAtDate: Calendar? = null
    private var startFinishing = false

    fun setOptions() {
        options.asReversed().forEach { viewState.addOption(it) }
    }

    fun onAdd(contentString: String) {
        if (!contentString.isBlank()) {
            options.add(contentString)
            viewState.addOption(contentString)
        }
    }

    fun removeOption(position: Int) {
        if (position != -1)
            options.removeAt(position)
    }

    fun moveOption(from: Int, to: Int) {
        val temp = options[from]
        options[from] = options[to]
        options[to] = temp
    }

    fun finishAdding(theme: String, description: String) {
        if (startFinishing) return

        val endsAt = endsAtDate
        if (theme.isBlank() || endsAt == null || options.isEmpty())
            viewState.error(resources.getString(R.string.input_error))
        else {
            startFinishing = true
            val createPollView = CreatePollView(theme, description, endsAt.time)
            val createPollOptions = options.map { CreatePollOptionView(it) }
            val job = pollRepository.createPoll(createPollView, createPollOptions.asReversed())
            jobs.add(job)
            job.start()
            job.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {
                    viewState.finishAdding()
                    startFinishing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

    fun setEndsDate(endsAt: Calendar) {
        endsAtDate = endsAt
    }
}