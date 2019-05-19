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

import android.content.Context
import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.entities.OnAddressesData
import com.mw.beam.beamwallet.core.entities.OnTxStatusData
import com.mw.beam.beamwallet.core.entities.TxDescription
import com.mw.beam.beamwallet.core.helpers.Category
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 1/21/19.
 */
interface SettingsContract {
    interface View : MvpView {
        fun init(runOnRandomNode: Boolean)
        fun sendMailWithLogs()
        fun changePass()
        fun showLockScreenSettingsDialog()
        fun showFingerprintSettings(isFingerprintEnabled: Boolean)
        fun getContext(): Context?
        fun closeDialog()
        fun updateLockScreenValue(millis: Long)
        fun updateConfirmTransactionValue(isConfirm: Boolean)
        fun showConfirmPasswordDialog(onConfirm: () -> Unit, onDismiss: () -> Unit)
        fun showNodeAddressDialog(nodeAddress: String?)
        fun showInvalidNodeAddressError()
        fun clearInvalidNodeAddressError()
        fun showClearDataDialog()
        fun setAllowOpenExternalLinkValue(allowOpen: Boolean)
        fun updateCategoryList(allCategory: List<Category>)
        fun navigateToEditCategory(categoryId: String)
        fun navigateToAddCategory()
        fun showClearDataAlert(clearAddresses: Boolean, clearContacts: Boolean, clearTransactions: Boolean)
    }

    interface Presenter : MvpPresenter<View> {
        fun onReportProblem()
        fun onChangePass()
        fun onShowLockScreenSettings()
        fun onChangeLockSettings(millis: Long)
        fun onDialogClosePressed()
        fun onChangeConfirmTransactionSettings(isConfirm: Boolean)
        fun onChangeFingerprintSettings(isEnabled: Boolean)
        fun onChangeRunOnRandomNode(isEnabled: Boolean)
        fun onChangeAllowOpenExternalLink(allowOpen: Boolean)
        fun onNodeAddressPressed()
        fun onChangeNodeAddress()
        fun onClearDataPressed()
        fun onDialogClearDataPressed(clearAddresses: Boolean, clearContacts: Boolean, clearTransactions: Boolean)
        fun onConfirmClearDataPressed(clearAddresses: Boolean, clearContacts: Boolean, clearTransactions: Boolean)
        fun onSaveNodeAddress(address: String?)
        fun onAddCategoryPressed()
        fun onCategoryPressed(categoryId: String)
    }

    interface Repository : MvpRepository {
        fun getLockScreenValue(): Long
        fun saveLockSettings(millis: Long)
        fun saveConfirmTransactionSettings(shouldConfirm: Boolean)
        fun saveEnableFingerprintSettings(isEnabled: Boolean)
        fun shouldConfirmTransaction(): Boolean
        fun isFingerPrintEnabled(): Boolean
        fun isAllowOpenExternalLink(): Boolean
        fun setAllowOpenExternalLink(allowOpen: Boolean)
        fun setNodeAddress(address: String)
        fun getSavedNodeAddress(): String?
        fun setRunOnRandomNode(random: Boolean)
        fun getCurrentNodeAddress(): String
        fun deleteAddress(addressId: String)
        fun deleteTransaction(txDescription: TxDescription?)
        fun getAddresses(): Subject<OnAddressesData>
        fun getTxStatus(): Subject<OnTxStatusData>
        fun getAllCategory(): List<Category>
    }
}
