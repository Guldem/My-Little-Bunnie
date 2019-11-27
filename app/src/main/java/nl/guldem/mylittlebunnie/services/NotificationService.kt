package nl.guldem.mylittlebunnie.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import nl.guldem.mylittlebunnie.R
import nl.guldem.mylittlebunnie.ui.CHANNEL_ID
import nl.guldem.mylittlebunnie.ui.MainActivity
import nl.guldem.mylittlebunnie.ui.home.Suprise
import nl.guldem.mylittlebunnie.util.extensions.nowInAmsterdam
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import java.util.*
import java.util.concurrent.TimeUnit

const val PRESENT_URI_KEY = "PRESENT_URI_KEY"

class NotificationService(private val context: Context) : KoinComponent {

    fun scheduleNotification(suprise: Suprise) {
        if (nowInAmsterdam() >= suprise.date) return
        val presentData = workDataOf(PRESENT_URI_KEY to suprise.supriseVideo.toString())
        val notifyRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInputData(presentData)
            .setInitialDelay(calculateDelayInMillis(suprise.date), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(notifyRequest)
    }

    private fun calculateDelayInMillis(datetime: DateTime): Long {
        return datetime.millis - nowInAmsterdam().millis
    }

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_title)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


class NotifyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        val presentUri = inputData.getString(PRESENT_URI_KEY)
        if (!presentUri.isNullOrEmpty()) {
            showNotification()
        }

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }

    private fun showNotification() {
            // Create an explicit intent for an Activity in your app
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("New Surprise!")
                .setContentText("There is a new surprise avaible!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(Random().nextInt(), builder.build())
            }
        }
    }

