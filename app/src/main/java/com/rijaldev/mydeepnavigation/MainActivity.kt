package com.rijaldev.mydeepnavigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.rijaldev.mydeepnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenDetail.setOnClickListener {
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_TITLE, getString(R.string.detail_title))
                putExtra(DetailActivity.EXTRA_MESSAGE, getString(R.string.detail_message))
                startActivity(this)
            }
        }

        showNotification(this, getString(R.string.notification_title), getString(R.string.notification_message), 110)
    }

    private fun showNotification(
        context: Context,
        title: String,
        msg: String,
        notificationId: Int
    ) {
        val channelId = "channel_notif"
        val channelName = "Navigation Channel"

        val notifDetailIntent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_TITLE, title)
            putExtra(DetailActivity.EXTRA_MESSAGE, msg)
        }

        val pendingIntent = TaskStackBuilder.create(this)
            .addParentStack(DetailActivity::class.java)
            .addNextIntent(notifDetailIntent)
            .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_baseline_email_24)
            .setContentText(msg)
            .setColor(ContextCompat.getColor(context, android.R.color.holo_purple))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            }

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }
}