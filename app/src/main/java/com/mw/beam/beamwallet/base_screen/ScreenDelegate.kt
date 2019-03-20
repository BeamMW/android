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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.Snackbar
import android.support.v4.app.SupportActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.core.helpers.QrHelper
import com.mw.beam.beamwallet.core.helpers.Status
import com.mw.beam.beamwallet.core.views.BeamButton

/**
 * Created by vain onnellinen on 12/4/18.
 */
class ScreenDelegate {
    private var alert: AlertDialog? = null
    private val QR_SIZE = 160.0
    private var qrDialog: AlertDialog? = null

    fun hideKeyboard(activity: SupportActivity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.findViewById<View>(android.R.id.content)?.windowToken, 0)
    }

    fun showKeyboard(activity: SupportActivity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun showSnackBar(status: Status, activity: SupportActivity) {
        showSnackBar(
                when (status) {
                    Status.STATUS_OK -> activity.getString(R.string.common_successful)
                    Status.STATUS_ERROR -> activity.getString(R.string.common_error)
                }, activity
        )
    }

    fun showSnackBar(message: String, activity: SupportActivity) {
        val snackBar = Snackbar.make(activity.findViewById(android.R.id.content) ?: return,
                message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.snack_bar_color))
        snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text).setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
        snackBar.show()
    }

    @SuppressLint("InflateParams")
    fun showAlert(message: String, title: String, btnConfirmText: String, btnCancelText: String?, onConfirm: () -> Unit, onCancel: () -> Unit, context: Context): AlertDialog? {
        val view = LayoutInflater.from(context).inflate(R.layout.common_alert_dialog, null)
        val alertTitle = view.findViewById<TextView>(R.id.title)
        val alertText = view.findViewById<TextView>(R.id.alertText)
        val btnConfirm = view.findViewById<TextView>(R.id.btnConfirm)
        val btnCancel = view.findViewById<TextView>(R.id.btnCancel)

        alertText.text = message
        alertTitle.text = title
        btnConfirm.text = btnConfirmText
        btnCancel.text = btnCancelText

        btnConfirm.setOnClickListener {
            onConfirm.invoke()
            alert?.dismiss()
        }
        btnCancel.setOnClickListener {
            onCancel.invoke()
            alert?.dismiss()
        }

        val dialog = AlertDialog.Builder(context).setView(view).show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert = dialog

        return alert
    }

    fun showQrCodeDialog(context: Context, token: String, copyClickListener: View.OnClickListener): AlertDialog? {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_receive, null)
        val qrView = view.findViewById<ImageView>(R.id.qrView)
        val tokenView = view.findViewById<TextView>(R.id.tokenView)
        val btnCopy = view.findViewById<BeamButton>(R.id.btnCopy)
        val close = view.findViewById<ImageView>(R.id.close)

        tokenView.text = token

        try {
            val metrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(metrics)
            val logicalDensity = metrics.density
            val px = Math.ceil(QR_SIZE * logicalDensity).toInt()

            qrView.setImageBitmap(QrHelper.textToImage(token, px, px,
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.common_text_color)))
        } catch (e: Exception) {
            return null
        }

        val dialog = AlertDialog.Builder(context).setView(view).show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnCopy.setOnClickListener(copyClickListener)
        close.setOnClickListener { dialog?.dismiss() }

        qrDialog = dialog
        return qrDialog
    }

    fun dismissAlert() {
        if (alert != null) {
            alert?.dismiss()
            alert = null
        }
    }

    fun dismissQrCodeDialog() {
        qrDialog?.let {
            it.dismiss()
            qrDialog = null
        }
    }
}
