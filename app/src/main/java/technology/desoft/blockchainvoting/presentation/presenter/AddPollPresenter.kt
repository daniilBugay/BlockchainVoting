package technology.desoft.blockchainvoting.presentation.presenter

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.CreatePollOptionView
import technology.desoft.blockchainvoting.model.network.polls.CreatePollView
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.presentation.view.AddPollView
import technology.desoft.blockchainvoting.ui.notification.FIREBASE_API_TOKEN
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
                sendNotification(theme)
                GlobalScope.launch(Dispatchers.Main) {
                    viewState.finishAdding()
                    startFinishing = false
                }
            }
        }
    }

    private fun sendNotification(pollTheme: String) {
        val mediaType = MediaType.get("application/json; charset=utf-8")
        val title = resources.getString(R.string.new_poll_title)
        val description = resources.getString(R.string.new_poll_description, pollTheme)
        val json = """
            {
                "to": "/topics/new_poll",
                "data": {
                    "title": "$title",
                    "description" : "$description"
                },
                "notification": {
                    "title": "$title",
                    "body": "$description"
                }
            }
        """.trimIndent()

        val client = OkHttpClient()
        val body = RequestBody.create(mediaType, json)
        val request = Request.Builder()
            .url("https://fcm.googleapis.com/fcm/send")
            .addHeader("Authorization", FIREBASE_API_TOKEN)
            .post(body)
            .build()
        client.newCall(request).execute().body()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }

    fun setEndsDate(endsAt: Calendar) {
        endsAtDate = endsAt
    }
}