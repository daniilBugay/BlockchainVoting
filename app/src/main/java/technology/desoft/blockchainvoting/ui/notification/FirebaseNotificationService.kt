package technology.desoft.blockchainvoting.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import technology.desoft.blockchainvoting.App
import technology.desoft.blockchainvoting.R
import technology.desoft.blockchainvoting.model.network.BASE_URL
import technology.desoft.blockchainvoting.ui.activity.MainActivity

class FirebaseNotificationService : FirebaseMessagingService() {
    private companion object {
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"
        const val CHANNEL_ID = "notification"
        const val TAG = "FirebaseService"
    }

    private lateinit var api: TokenPostApi

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BASE_URL/")
            .build()
        api = retrofit.create(TokenPostApi::class.java)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        Log.d(TAG, "MessageReceive")
        val data = message?.data ?: return
        val title = data[TITLE_KEY] ?: return
        val description = data[DESCRIPTION_KEY] ?: return
        sendNotification(title, description)
    }

    private fun sendNotification(title: String, body: String) {
        val notification = makeNotifications(this, title, body)
        notification.send(title)
    }

    private fun makeNotifications(context: Context, title: String, text: String): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(
            context,
            "$title${System.currentTimeMillis()}"
        )
        val notificationBitmap = BitmapFactory.decodeResource(resources, R.drawable.image_votes)
        notificationBuilder.setAutoCancel(true).setLargeIcon(notificationBitmap)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setContentText(text)
        return notificationBuilder
    }

    private fun NotificationCompat.Builder.send(title: String) {
        val notificationManager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
            setChannelId(CHANNEL_ID)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), build())
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        token ?: return
        Log.d(TAG, "Token: $token")
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val app = application as App
        val userId = app.userProvider.userId ?: return
        GlobalScope.launch {
            api.postToken(TokenPostApi.UserIdAndToken(userId, token)).await()
        }.start()
    }
}