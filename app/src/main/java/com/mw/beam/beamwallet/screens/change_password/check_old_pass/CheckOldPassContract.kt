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

package com.mw.beam.beamwallet.screens.change_password.check_old_pass

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView

/**
 * Created by vain onnellinen on 3/14/19.
 */
interface CheckOldPassContract {
    interface View : MvpView {
        fun init()
        fun hasErrors(): Boolean
        fun clearErrors()
        fun getPass(): String
        fun showWrongPassError()
        fun showNewPassFragment()
    }

    interface Presenter : MvpPresenter<View> {
        fun onPassChanged(pass: String?)
        fun onNext()
    }

    interface Repository : MvpRepository {
        fun checkPass(pass: String?): Boolean
    }
}
