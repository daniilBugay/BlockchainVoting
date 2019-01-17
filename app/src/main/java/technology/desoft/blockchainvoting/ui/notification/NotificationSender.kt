package technology.desoft.blockchainvoting.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.BASE_URL
import technology.desoft.blockchainvoting.model.network.polls.Poll
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.polls.RetrofitPollRepository
import technology.desoft.blockchainvoting.model.network.user.Token
import technology.desoft.blockchainvoting.ui.activity.MainActivity

class NotificationSender: BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "notification"
        private const val USER_TOKEN_KEY = "token"
    }

    private var polls: List<Poll>? = null
    private val pollRepository: PollRepository
    private val jobs = mutableListOf<Job>()

    init {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BASE_URL/")
            .build()
        pollRepository  = RetrofitPollRepository(retrofit)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val token = intent?.getStringExtra(USER_TOKEN_KEY) ?: return
        pollRepository.setToken(Token(token, false))
        sendNotifications(context)
    }

    private fun sendNotifications(context: Context?) {
        val job = GlobalScope.launch(Dispatchers.IO) {
            val loadedPolls = pollRepository.getPolls().await()
            val ids = polls?.map(Poll::id)
            val newPolls = loadedPolls?.filter { newPoll -> ids?.contains(newPoll.id) == false }
            val notificationManager
                    = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            newPolls?.forEach {
                val notification = makeNotifications(context, it)
                GlobalScope.launch(Dispatchers.Main) {
                    send(notificationManager, notification, it)
                }.join()
            }
            polls = loadedPolls
        }
        jobs.add(job)
        job.start()
    }

    private fun send(
        notificationManager: NotificationManager,
        builder: NotificationCompat.Builder,
        poll: Poll
    ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, poll.theme, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(CHANNEL_ID)
        }
        notificationManager.notify(poll.id.toInt(), builder.build())
    }

    private fun makeNotifications(context: Context, poll: Poll): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentTitle(poll.theme)
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setContentInfo(
            context.resources.getString(R.string.new_poll_description, poll.theme)
        )
        return notificationBuilder
    }
}