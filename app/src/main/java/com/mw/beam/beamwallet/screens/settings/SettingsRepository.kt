/*
 * // Copyright 2018 Beam Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 */

package com.mw.beam.beamwallet.screens.settings

import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.base_screen.BaseRepository
import com.mw.beam.beamwallet.core.App
import com.mw.beam.beamwallet.core.helpers.PreferencesManager
import com.mw.beam.beamwallet.core.utils.LockScreenManager
import com.mw.beam.beamwallet.core.utils.isLessMinute
import java.util.concurrent.TimeUnit

/**
 * Created by vain onnellinen on 1/21/19.
 */
class SettingsRepository : BaseRepository(), SettingsContract.Repository {
    override fun getLockScreenStringValue(): String {
        val context = App.self.applicationContext
        val time = LockScreenManager.getCurrentValue(context)
        return when {
            time <= LockScreenManager.LOCK_SCREEN_NEVER_VALUE -> context.getString(R.string.never)
            time.isLessMinute() -> context.getString(R.string.after_seconds, TimeUnit.MILLISECONDS.toSeconds(time).toString())
            else -> context.getString(R.string.after_minute, TimeUnit.MILLISECONDS.toMinutes(time).toString())
        }
    }

    override fun saveLockSettings(millis: Long) {
        LockScreenManager.updateLockScreenSettings(App.self, millis)
    }

    override fun saveConfirmTransactionSettings(isConfirm: Boolean) {
        PreferencesManager.putBoolean(PreferencesManager.KEY_IS_SENDING_CONFIRM_ENABLED, isConfirm)
    }

    override fun isConfirmTransaction(): Boolean {
        return PreferencesManager.getBoolean(PreferencesManager.KEY_IS_SENDING_CONFIRM_ENABLED)
    }
}
