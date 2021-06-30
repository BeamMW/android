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

package com.mw.beam.beamwallet.screens.search_transaction

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.entities.OnAddressesData
import com.mw.beam.beamwallet.core.entities.OnTxStatusData
import com.mw.beam.beamwallet.core.entities.TxDescription
import com.mw.beam.beamwallet.core.entities.WalletAddress
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface SearchTransactionContract {
    interface View: MvpView {
        fun init()
        fun getAssetId():Int
        fun clearSearchText()
        fun configTransactions(transactions: List<TxDescription>, isEnablePrivacyMode: Boolean, searchText: String?)
        fun setClearButtonVisible(isVisible: Boolean)
        fun showTransactionDetails(txId: String)
    }
    interface Presenter: MvpPresenter<View> {
        fun onSearchTextChanged(text: String)
        fun onClearPressed()
        fun onTransactionPressed(txDescription: TxDescription)
    }
    interface Repository: MvpRepository {
    }
}