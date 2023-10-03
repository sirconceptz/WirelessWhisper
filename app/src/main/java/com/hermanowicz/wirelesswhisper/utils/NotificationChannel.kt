package com.hermanowicz.wirelesswhisper.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NotificationChannel {
    companion object {
        fun createNotificationChannel(
            context: Context,
            channelId: String,
            channelName: String
        ): String {
            val chan = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_NONE
            )
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            return channelId
        }
    }
}
