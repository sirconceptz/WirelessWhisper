package com.hermanowicz.wirelesswhisper.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import me.leolin.shortcutbadger.ShortcutBadger
import javax.inject.Inject

class SetAppIconUnreadMessageCounterUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : (Int?) -> Unit {
    override fun invoke(counter: Int?) {
        if (counter == null) {
            ShortcutBadger.removeCount(context)
        } else {
            ShortcutBadger.applyCount(context, counter)
        }
    }
}
