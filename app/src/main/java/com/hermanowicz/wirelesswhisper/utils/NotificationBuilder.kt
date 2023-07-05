package com.hermanowicz.wirelesswhisper.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.hermanowicz.wirelesswhisper.MainActivity
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.utils.Constants.GROUP_NOTIFICATIONS
import com.hermanowicz.wirelesswhisper.utils.Constants.NEW_MESSAGE_CHANNEL_ID
import com.hermanowicz.wirelesswhisper.utils.NotificationChannel.Companion.createNotificationChannel
import dagger.hilt.android.qualifiers.ApplicationContext

object NotificationBuilder {
    fun buildNotification(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val title = context.getString(R.string.new_message)
        val description = context.getText(R.string.you_received_new_message)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle(title).bigText(description)

        val notificationChannel =
            createNotificationChannel(context, NEW_MESSAGE_CHANNEL_ID, context.getString(R.string.new_message))

        val notificationCompatBuilder =
            NotificationCompat.Builder(context, notificationChannel)

        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            context,
            42,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(notifyPendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .setGroup(GROUP_NOTIFICATIONS)
    }
}
