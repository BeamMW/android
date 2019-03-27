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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import com.eightsines.holycycle.app.ViewControllerAppCompatActivity
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.core.Api
import com.mw.beam.beamwallet.core.App
import com.mw.beam.beamwallet.core.AppConfig
import com.mw.beam.beamwallet.core.helpers.Status
import com.mw.beam.beamwallet.core.utils.LockScreenManager
import com.mw.beam.beamwallet.core.views.BeamToolbar
import com.mw.beam.beamwallet.screens.welcome_screen.WelcomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * Created by vain onnellinen on 10/1/18.
 */
abstract class BaseActivity<T : BasePresenter<out MvpView, out MvpRepository>> : ViewControllerAppCompatActivity(), MvpView {
    private lateinit var presenter: T
    private val delegate = ScreenDelegate()
    private var isActivityStopped = false
    private var isExpireLockScreenTime = false
    private val lockScreenReceiver=  object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (ensureState()) {
                if (isActivityStopped) {
                    isExpireLockScreenTime = true
                } else {
                    closeWallet()
                }
            }
        }
    }

    protected fun showFragment(
            fragment: Fragment,
            tag: String,
            clearToTag: String?,
            clearInclusive: Boolean
    ) {
        drawerLayout?.closeDrawer(Gravity.START)
        val fragmentManager = supportFragmentManager
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment == null || tag != currentFragment.tag) {
            if (clearToTag != null || clearInclusive) {
                fragmentManager.popBackStack(
                        clearToTag,
                        if (clearInclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
                )
            }

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment, tag)
            transaction.addToBackStack(tag)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }
    }

    override fun showAlert(message: String, btnConfirmText: String, onConfirm: () -> Unit, title: String?, btnCancelText: String?, onCancel: () -> Unit): AlertDialog? {
        return delegate.showAlert(message, btnConfirmText, onConfirm, title, btnCancelText, onCancel, this)
    }

    override fun showSnackBar(status: Status) = delegate.showSnackBar(status, this)
    override fun showSnackBar(message: String) = delegate.showSnackBar(message, this)
    override fun showSnackBar(message: String, textColor : Int) = delegate.showSnackBar(message, textColor, this)
    override fun showKeyboard() = delegate.showKeyboard(this)
    override fun hideKeyboard() = delegate.hideKeyboard(this)
    override fun dismissAlert() = delegate.dismissAlert()

    override fun initToolbar(title: String?, hasBackArrow: Boolean?, hasStatus: Boolean) {
        val toolbarLayout = this.findViewById<BeamToolbar>(R.id.toolbarLayout) ?: return
        setSupportActionBar(toolbarLayout.toolbar)
        supportActionBar?.title = title
        toolbarLayout.hasStatus = hasStatus

        if (hasBackArrow != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(hasBackArrow)

            if (hasBackArrow) {
                toolbarLayout.toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }
    }

    open fun ensureState(): Boolean = App.wallet != null

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            presenter.onClose()
            finish()
            return
        }

        super.onBackPressed()
    }

    override fun addListeners() {
    }

    override fun clearListeners() {
    }

    override fun configStatus(isConnected: Boolean) {
        val toolbarLayout = this.findViewById<BeamToolbar>(R.id.toolbarLayout) ?: return

        toolbarLayout.statusIcon.setImageDrawable(
                if (isConnected) ContextCompat.getDrawable(this, R.drawable.status_connected)
                else ContextCompat.getDrawable(this, R.drawable.status_error))
        toolbarLayout.status.text =
                if (isConnected) getString(R.string.common_status_online)
                else String.format(getString(R.string.common_status_error), AppConfig.NODE_ADDRESS)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onControllerCreate(extras: Bundle?) {
        super.onControllerCreate(extras)
        presenter = initPresenter() as T

        if (!ensureState()) {
            startActivity(Intent(this, WelcomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )

            finish()
        } else {
            presenter.onCreate()
        }
        registerReceiver(lockScreenReceiver, IntentFilter(LockScreenManager.LOCK_SCREEN_ACTION))
    }

    override fun onControllerContentViewCreated() {
        super.onControllerContentViewCreated()
        presenter.onViewCreated()
    }

    override fun onControllerStart() {
        super.onControllerStart()
        presenter.onStart()
    }

    override fun onControllerResume() {
        if (isExpireLockScreenTime) {
            closeWallet()
        } else {
            isActivityStopped = false
        }
        super.onControllerResume()
        presenter.onResume()
    }

    override fun onControllerPause() {
        presenter.onPause()
        super.onControllerPause()
    }

    override fun onControllerStop() {
        isActivityStopped = true
        presenter.onStop()
        super.onControllerStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        unregisterReceiver(lockScreenReceiver)
        super.onDestroy()
    }

    private fun closeWallet() {
        startActivity(Intent(applicationContext, WelcomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
        Handler().postDelayed(Api::closeWallet, TimeUnit.SECONDS.toMillis(1)) //TODO: crash in native lib without delay
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        LockScreenManager.restartTimer(applicationContext)
    }
}
