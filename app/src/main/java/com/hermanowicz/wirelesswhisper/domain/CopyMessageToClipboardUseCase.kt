package com.hermanowicz.wirelesswhisper.domain

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.hermanowicz.wirelesswhisper.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CopyMessageToClipboardUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : (String) -> Unit {
    override fun invoke(message: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("message", message)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.message_copied_to_clipboard), Toast.LENGTH_LONG).show()
    }
}
