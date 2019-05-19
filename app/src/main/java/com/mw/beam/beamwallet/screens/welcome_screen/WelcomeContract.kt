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

package com.mw.beam.beamwallet.screens.welcome_screen

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.helpers.WelcomeMode

/**
 * Created by vain onnellinen on 10/19/18.
 */
interface WelcomeContract {
    interface View : MvpView {
        fun showOpenFragment()
        fun showDescriptionFragment()
        fun showPasswordsFragment(phrases: Array<String>, mode: WelcomeMode)
        fun showSeedFragment()
        fun showRestoreFragment()
        fun showCreateFragment()
        fun showValidationFragment(phrases: Array<String>)
        fun showProgressFragment(mode: WelcomeMode, pass : String, seed: Array<String>? = null)
        fun showMainActivity()
        fun finishNotRootTask()
    }

    interface Presenter : MvpPresenter<View> {
        fun onCreateWallet()
        fun onRestoreWallet()
        fun onGenerateSeed()
        fun onShowWallet()
        fun onOpenWallet(mode: WelcomeMode, pass: String, seed: Array<String>? = null)
        fun onChangeWallet()
        fun onProceedToPasswords(phrases: Array<String>, mode: WelcomeMode)
        fun onProceedToValidation(phrases: Array<String>)
    }

    interface Repository : MvpRepository {
        fun isWalletInitialized(): Boolean
    }
}
