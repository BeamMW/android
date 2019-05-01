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

package com.mw.beam.beamwallet.screens.utxo

import android.view.Menu
import android.view.MenuInflater
import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.entities.SystemState
import com.mw.beam.beamwallet.core.entities.Utxo
import com.mw.beam.beamwallet.core.entities.WalletStatus
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 10/2/18.
 */
interface UtxoContract {
    interface View : MvpView {
        fun init()
        fun updateUtxos(utxos: List<Utxo>)
        fun showUtxoDetails(utxo: Utxo)
        fun updateBlockchainInfo(systemState: SystemState)
        fun showActivatePrivacyModeDialog()
        fun configPrivacyStatus(isEnable: Boolean)
        fun createOptionsMenu(menu: Menu?, inflater: MenuInflater?, isEnablePrivacyMode: Boolean)
    }

    interface Presenter : MvpPresenter<View> {
        fun onUtxoPressed(utxo: Utxo)
        fun onChangePrivacyModePressed()
        fun onPrivacyModeActivated()
        fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
        fun onCancelDialog()
    }

    interface Repository : MvpRepository {
        fun getUtxoUpdated(): Subject<List<Utxo>>
        fun getWalletStatus(): Subject<WalletStatus>
        fun isNeedConfirmEnablePrivacyMode(): Boolean
    }
}
