package s18alg.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import s18alg.myapplication.activity.MainActivity
import s18alg.myapplication.model.Profile
import s18alg.myapplication.model.TargetWebsite
import java.net.URI
import java.time.DayOfWeek
import java.time.LocalDateTime

class NotificationService(mContext: Context? = null) {
    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var pendingIntent: PendingIntent? = null
    private var intentMainActivity = Intent(mContext, MainActivity::class.java)

    private val downTargets: HashMap<Int, TargetWebsite> = hashMapOf()

    private val CHANNEL_ID = "targetdown.notification"

    init {
        if (mContext != null) {
            notificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_service_in_trouble)
                    .setContentTitle("My title")
                    .setContentText("Stuff is going bad")
                    .setChannelId(CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager = getSystemService(mContext, NotificationManager::class.java)
            createNotificationChannel()

            // Initialise intent to make the notification point to mainActivity
            intentMainActivity.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            pendingIntent = PendingIntent.getActivity(mContext, 0, intentMainActivity, 0)
            notificationBuilder?.setContentIntent(pendingIntent)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "littleAdmin watcher", NotificationManager.IMPORTANCE_DEFAULT)

        channel.description = "Notify when a target goes down"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        notificationManager?.createNotificationChannel(channel)
    }

    fun isTargetTobeNotified(target: TargetWebsite, timeProvider: LocalDateTime = LocalDateTime.now()): Boolean {
        return when (target.profile) {
            Profile.Personal -> true
            Profile.Other -> false
            Profile.Professional -> {
                if ((timeProvider.dayOfWeek == DayOfWeek.of(6) || timeProvider.dayOfWeek == DayOfWeek.of(7))
                || timeProvider.hour !in 8..17) {
                    return false
                }
                return true
            }
        }
    }

    fun notifyTarget() {
        if (!downTargets.isEmpty()) {
            var text = ""

            downTargets.forEach {
                if (isTargetTobeNotified(it.value)) {
                    text += URI(it.value.uri).host + "\n"
                }
            }
            if (text != "") {
                notificationBuilder?.setContentTitle(String.format("%d target have gone down", downTargets.size))
                notificationBuilder?.setContentText(text)
                notificationManager?.notify(101, notificationBuilder?.build())
            }
        } else {
            notificationManager?.cancel(101)
        }
    }

    fun addTargetToNotification(target: TargetWebsite) {
        downTargets[target.id] = target
    }

    fun removeTargetFromNotification(target: TargetWebsite) {
        if (downTargets.containsKey(target.id)) {
            downTargets.remove(target.id)
        }
    }

    fun getDownTargetSize() : Int {
        return downTargets.size
    }
}