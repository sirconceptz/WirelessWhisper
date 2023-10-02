package com.hermanowicz.wirelesswhisper.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class GoToPermissionSettingsUseCase {
    companion object {
        fun invoke(context: Context) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            context.startActivity(intent)
        }
    }
}
