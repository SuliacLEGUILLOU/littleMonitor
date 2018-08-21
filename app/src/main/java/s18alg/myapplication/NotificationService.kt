package s18alg.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import s18alg.myapplication.activity.MainActivity
import s18alg.myapplication.model.TargetWebsite
import java.net.URI

class NotificationService(context: Context) {
    private var notificationManager: NotificationManager? = null
    private var pendingIntent: PendingIntent

    private var intentMainActivity = Intent(context, MainActivity::class.java)
    private val mContext = context
    private val targetToNotify: HashMap<Int, TargetWebsite> = hashMapOf()

    private val CHANNEL_ID = "targetdown.notification"

    private val notificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_service_in_trouble)
            .setContentTitle("My title")
            .setContentText("Stuff is going bad")
            .setChannelId(CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    init {
        notificationManager = getSystemService(mContext, NotificationManager::class.java)
        createNotificationChannel()

        // Initialise intent to make the notification point to mainActivity
        intentMainActivity.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        pendingIntent = PendingIntent.getActivity(mContext, 0, intentMainActivity, 0)
        notificationBuilder.setContentIntent(pendingIntent)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "littleAdmin watcher", NotificationManager.IMPORTANCE_DEFAULT)

        channel.description = "Notify when a target goes down"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        notificationManager?.createNotificationChannel(channel)
    }

    fun notifyTarget() {
        if (!targetToNotify.isEmpty()) {
            var text = ""

            targetToNotify.forEach {
                text += URI(it.value.uri).host + "\n"
            }
            notificationBuilder.setContentTitle(String.format("%d target have gone down", targetToNotify.size))
            notificationBuilder.setContentText(text)
            notificationManager?.notify(101, notificationBuilder.build())
        } else {
            notificationManager?.cancel(101)
        }
    }

    fun addTargetToNotificaiton(target: TargetWebsite) {
        targetToNotify[target.id] = target
    }

    fun removeTargetFromNotification(target: TargetWebsite) {
        if (targetToNotify.containsKey(target.id)) {
            targetToNotify.remove(target.id)
        }
    }
}