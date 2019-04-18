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
package com.mw.beam.beamwallet.base_screen

import com.mw.beam.beamwallet.core.Api
import com.mw.beam.beamwallet.core.App
import com.mw.beam.beamwallet.core.AppConfig
import com.mw.beam.beamwallet.core.entities.OnSyncProgressData
import com.mw.beam.beamwallet.core.entities.Wallet
import com.mw.beam.beamwallet.core.helpers.PreferencesManager
import com.mw.beam.beamwallet.core.helpers.Status
import com.mw.beam.beamwallet.core.listeners.WalletListener
import com.mw.beam.beamwallet.core.utils.LogUtils
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 10/1/18.
 */
open class BaseRepository : MvpRepository {

    override val wallet: Wallet?
        get() = App.wallet

    override fun openWallet(pass: String?): Status {
        var result = Status.STATUS_ERROR

        if (!pass.isNullOrBlank()) {
            AppConfig.NODE_ADDRESS = Api.getDefaultPeers().random()
            App.wallet = Api.openWallet(AppConfig.APP_VERSION, AppConfig.NODE_ADDRESS, AppConfig.DB_PATH, pass)

            if (wallet != null) {
                PreferencesManager.putString(PreferencesManager.KEY_PASSWORD, pass)
                result = Status.STATUS_OK
            }
        }

        LogUtils.logResponse(result, "openWallet")
        return result
    }

    override fun closeWallet() {
        getResult("closeWallet") {
            if (Api.isWalletRunning()) {
                App.wallet = null
                Api.closeWallet()
            }
        }
    }

    override fun getNodeConnectionStatusChanged(): Subject<Boolean> {
        return getResult(WalletListener.subOnNodeConnectedStatusChanged, "getNodeConnectionStatusChanged")
    }

    override fun getNodeConnectionFailed(): Subject<Any> {
        return getResult(WalletListener.subOnNodeConnectionFailed, "getNodeConnectionFailed")
    }

    override fun getSyncProgressUpdated(): Subject<OnSyncProgressData> {
        return getResult(WalletListener.subOnSyncProgressUpdated, "getSyncProgressUpdated")
    }

    fun <T> getResult(subject: Subject<T>, requestName: String, additionalInfo: String = "", block: () -> Unit = {}): Subject<T> {
        LogUtils.log(StringBuilder()
                .append(LogUtils.LOG_REQUEST)
                .append(" ")
                .append(requestName)
                .append("\n")
                .append(additionalInfo)
                .append("\n")
                .append("--------------------------")
                .append("\n").toString())
        block.invoke()
        return subject
    }

    fun getResult(requestName: String, additionalInfo: String = "", block: () -> Unit = {}) {
        LogUtils.log(StringBuilder()
                .append(LogUtils.LOG_REQUEST)
                .append(" ")
                .append(requestName)
                .append("\n")
                .append(additionalInfo)
                .append("\n")
                .append("--------------------------")
                .append("\n").toString())
        block.invoke()
    }
}
