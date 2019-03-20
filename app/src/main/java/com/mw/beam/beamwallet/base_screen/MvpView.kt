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

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.mw.beam.beamwallet.core.helpers.Status

/**
 * Created by vain onnellinen on 10/1/18.
 */
interface MvpView {
    fun showKeyboard()
    fun hideKeyboard()
    fun showSnackBar(status: Status)
    fun showSnackBar(message: String)
    fun showAlert(message: String, title: String, btnConfirmText: String, btnCancelText: String? = null, onConfirm: () -> Unit = {}, onCancel: () -> Unit = {}): AlertDialog?
    fun dismissAlert()
    fun initPresenter(): BasePresenter<out MvpView, out MvpRepository>
    fun initToolbar(title: String?, hasBackArrow: Boolean?, hasStatus: Boolean)
    fun configStatus(isConnected: Boolean)
    fun getToolbarTitle(): String?
    fun addListeners()
    fun clearListeners()
    fun showQrCodeDialog(context: Context, token: String, copyClickListener: View.OnClickListener): AlertDialog?
    fun dismissQrCodeDialog()
}
