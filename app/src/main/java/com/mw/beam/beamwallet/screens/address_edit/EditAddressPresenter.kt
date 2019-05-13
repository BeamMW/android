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

package com.mw.beam.beamwallet.screens.address_edit

import com.mw.beam.beamwallet.base_screen.BasePresenter
import com.mw.beam.beamwallet.core.helpers.ExpirePeriod

/**
 * Created by vain onnellinen on 3/5/19.
 */
class EditAddressPresenter(currentView: EditAddressContract.View, currentRepository: EditAddressContract.Repository, private val state: EditAddressState)
    : BasePresenter<EditAddressContract.View, EditAddressContract.Repository>(currentView, currentRepository),
        EditAddressContract.Presenter {

    override fun onViewCreated() {
        super.onViewCreated()
        state.address = view?.getAddress()
        state.chosenPeriod = if (state.address!!.duration == 0L) ExpirePeriod.NEVER else ExpirePeriod.DAY
        state.tempComment = state.address?.label ?: ""

        view?.init(state.address ?: return)
    }

    override fun onSwitchCheckedChange(isChecked: Boolean) {
        val isExpired = state.address?.isExpired ?: return

        if (isExpired) {
            state.shouldActivateNow = isChecked
            view?.configExpireSpinnerVisibility(isChecked)
            view?.configSaveButton(shouldEnableButton())
        } else {
            state.shouldExpireNow = isChecked
            view?.configExpireSpinnerTime(isChecked)
            view?.configSaveButton(shouldEnableButton())
        }
    }

    override fun onExpirePeriodChanged(period: ExpirePeriod) {
        state.chosenPeriod = period
        view?.configSaveButton(shouldEnableButton())
    }

    override fun onChangeComment(comment: String) {
        state.tempComment = comment.trim()
        view?.configSaveButton(shouldEnableButton())
    }

    private fun shouldEnableButton(): Boolean {
        val isExpired = state.address?.isExpired ?: return false

        val isExpireChanged =  if (isExpired) {
            state.shouldActivateNow
        } else {
            if (state.shouldExpireNow) {
                true
            } else {
                when {
                    state.address!!.duration == 0L && state.chosenPeriod == ExpirePeriod.DAY -> true
                    state.address!!.duration != 0L && state.chosenPeriod == ExpirePeriod.NEVER -> true
                    else -> false
                }
            }
        }

        val isCommentChanged = state.tempComment != state.address?.label ?: return false

        return isExpireChanged || isCommentChanged
    }

    override fun onSavePressed() {
        val address = state.address ?: return
        address.label = state.tempComment.trim()

        if (address.isExpired) {
            if (state.shouldActivateNow) {
                repository.saveAddress(addr = address.walletID, name = address.label, makeActive = true, makeExpired = false, isNever = state.chosenPeriod == ExpirePeriod.NEVER)
            }
        } else {
            if (state.shouldExpireNow) {
                repository.saveAddress(addr = address.walletID, name = address.label, makeActive = false, makeExpired = true, isNever = address.duration == 0L)
            } else {
                when {
                    state.chosenPeriod == ExpirePeriod.NEVER -> repository.saveAddress(addr = address.walletID, name = address.label, makeActive = false, makeExpired = false, isNever = true)
                    state.chosenPeriod == ExpirePeriod.DAY -> repository.saveAddress(addr = address.walletID, name = address.label, makeActive = true, makeExpired = false, isNever = false)
                }
            }
        }

        view?.finishScreen()
    }
}

