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

import com.mw.beam.beamwallet.base_screen.BasePresenter

/**
 * Created by vain onnellinen on 1/21/19.
 */
class SettingsPresenter(currentView: SettingsContract.View, currentRepository: SettingsContract.Repository)
    : BasePresenter<SettingsContract.View, SettingsContract.Repository>(currentView, currentRepository),
        SettingsContract.Presenter {

    override fun onViewCreated() {
        super.onViewCreated()
        view?.init()
        view?.updateLockScreenValue(repository.getLockScreenStringValue())
        view?.updateConfirmTransactionValue(repository.isConfirmTransaction())
    }

    override fun onReportProblem() {
        view?.sendMailWithLogs()
    }

    override fun onChangePass() {
        view?.changePass()
    }

    override fun hasBackArrow(): Boolean? = null
    override fun hasStatus(): Boolean = true

    override fun showLockScreenSettings() {
        view?.showLockScreenSettingsDialog()
    }

    override fun onChangeLockSettings(millis: Long) {
        repository.saveLockSettings(millis)
        view?.apply {
            updateLockScreenValue(repository.getLockScreenStringValue())
            closeDialog()
        }
    }

    override fun onChangeConfirmTransactionSettings(isConfirm: Boolean) {
        repository.saveConfirmTransactionSettings(isConfirm)
    }

    override fun onDialogClosePressed() {
        view?.closeDialog()
    }

    override fun onDestroy() {
        view?.closeDialog()
        super.onDestroy()
    }
}
