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

package com.mw.beam.beamwallet.screens.change_address

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.entities.OnAddressesData
import com.mw.beam.beamwallet.core.entities.WalletAddress
import com.mw.beam.beamwallet.core.helpers.TrashManager
import io.reactivex.subjects.Subject

interface ChangeAddressContract {
    interface View: MvpView {
        fun isFromReceive(): Boolean
        fun init(state: ViewState, generatedAddress: WalletAddress?)
        fun getGeneratedAddress(): WalletAddress?
        fun updateList(items: List<WalletAddress>)
        fun back(walletAddress: WalletAddress?)
        fun setAddress(address: String)
        fun getSearchText(): String
        fun showNotBeamAddressError()
    }

    interface Presenter: MvpPresenter<View> {
        fun onChangeSearchText(text: String)
        fun onItemPressed(walletAddress: WalletAddress)
    }

    interface Repository: MvpRepository {
        fun getAddresses(): Subject<OnAddressesData>
        fun getTrashSubject(): Subject<TrashManager.Action>
        fun getAllAddressesInTrash(): List<WalletAddress>
    }

    enum class ViewState {
        Send, Receive
    }
}