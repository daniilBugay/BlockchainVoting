package technology.desoft.blockchainvoting.ui.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.polls.Poll
import technology.desoft.blockchainvoting.ui.activity.MainActivity
import java.util.*

class NotificationService: Service() {
    private var timerTask: TimerTask? = null
    private var polls: List<Poll>? = null
    private val handler = Handler()

    private val pollRepository get() = (application as App).pollRepository
    private val jobs = mutableListOf<Job>()


    companion object {
        private const val PERIOD_IN_MILLIS = 1_000L
        private const val CHANNEL_ID = "notification"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initTimer()
    }

    private fun initTimer(){
        if (timerTask == null)
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post { sendNotifications() }
            }
        }
    }

    private fun sendNotifications() {
        val job = GlobalScope.launch(Dispatchers.IO) {
            val loadedPolls = pollRepository.getPolls().await()
            val ids = polls?.map(Poll::id)
            val newPolls = loadedPolls?.filter { newPoll -> ids?.contains(newPoll.id) == false }
            val notificationManager
                    = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            newPolls?.forEach {
                val notification = makeNotifications(it)
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

    private fun makeNotifications(poll: Poll): NotificationCompat.Builder {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentTitle(poll.theme)
        notificationBuilder.setSmallIcon(R.drawable.ic_app_white)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setContentInfo(
            resources.getString(R.string.new_poll_description, poll.theme)
        )
        return notificationBuilder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startTimer()

        return START_STICKY
    }

    private fun startTimer() {
        val timer = Timer()

        try {
            timer.schedule(timerTask, 5000L, PERIOD_IN_MILLIS)
        } catch (e: Exception){
            
        }
    }

    override fun onDestroy() {
        stopTimer()
        jobs.forEach(Job::cancel)
        super.onDestroy()
    }

    private fun stopTimer(){
        timerTask?.cancel()
    }
}