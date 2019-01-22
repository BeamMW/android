// Copyright 2018 Beam Development
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.mw.beam.beamwallet.welcomeScreen.welcomeRestore

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.WindowManager
import android.widget.GridLayout
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.baseScreen.BaseFragment
import com.mw.beam.beamwallet.baseScreen.BasePresenter
import com.mw.beam.beamwallet.baseScreen.MvpRepository
import com.mw.beam.beamwallet.baseScreen.MvpView
import com.mw.beam.beamwallet.core.views.BeamPhraseInput
import com.mw.beam.beamwallet.core.watchers.TextWatcher
import kotlinx.android.synthetic.main.common_phrase_input.view.*
import kotlinx.android.synthetic.main.fragment_welcome_restore.*

/**
 * Created by vain onnellinen on 11/5/18.
 */
class WelcomeRestoreFragment : BaseFragment<WelcomeRestorePresenter>(), WelcomeRestoreContract.View {
    private lateinit var presenter: WelcomeRestorePresenter

    companion object {
        fun newInstance() = WelcomeRestoreFragment().apply { arguments = Bundle() }
        fun getFragmentTag(): String = WelcomeRestoreFragment::class.java.simpleName
    }

    override fun onControllerGetContentLayoutId() = R.layout.fragment_welcome_restore
    override fun getToolbarTitle(): String = getString(R.string.welcome_restore_title)

    override fun init() {
        btnRestore.isEnabled = false
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun addListeners() {
        btnRestore.setOnClickListener {
            if (it.isEnabled) {
                presenter.onRestorePressed()
            }
        }
    }

    override fun showPasswordsFragment(phrases: Array<String>) = (activity as WelcomeRestoreHandler).proceedToPasswords(phrases)

    override fun configPhrases(phrasesCount: Int) {
        val sideOffset: Int = resources.getDimensionPixelSize(R.dimen.welcome_grid_element_side_offset)
        val topOffset: Int = resources.getDimensionPixelSize(R.dimen.welcome_grid_element_top_offset)
        var columnIndex = 0
        var rowIndex = 0
        phrasesLayout.rowCount = phrasesCount / 2

        for (i in 1..phrasesCount) {
            if (columnIndex == phrasesLayout.columnCount) {
                columnIndex = 0
                rowIndex++
            }

            phrasesLayout.addView(configPhrase(i, rowIndex, columnIndex, sideOffset, topOffset))
            columnIndex++
        }
    }

    private fun configPhrase(number: Int, rowIndex: Int, columnIndex: Int, sideOffset: Int, topOffset: Int): View? {
        val context = context ?: return null

        val phrase = BeamPhraseInput(context)
        phrase.number = number

        val params = GridLayout.LayoutParams()
        params.height = GridLayout.LayoutParams.WRAP_CONTENT
        params.width = GridLayout.LayoutParams.WRAP_CONTENT
        params.columnSpec = GridLayout.spec(columnIndex, 1f)
        params.rowSpec = GridLayout.spec(rowIndex)
        params.topMargin = topOffset

        when (columnIndex) {
            0 -> params.rightMargin = sideOffset
            1 -> params.leftMargin = sideOffset
        }

        phrase.layoutParams = params

        phrase.phraseView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.onPhraseChanged()
            }
        })

        return phrase
    }

    override fun handleRestoreButton() {
        btnRestore.isEnabled = arePhrasesFilled()
    }

    override fun getPhrase(): Array<String> {
        val phrasesList = ArrayList<String>()

        for (i in 0 until phrasesLayout.childCount) {
            phrasesList.add((phrasesLayout.getChildAt(i) as BeamPhraseInput).phraseView.text.toString())
        }

        return phrasesList.toTypedArray()
    }

    private fun arePhrasesFilled(): Boolean {
        for (i in 0 until phrasesLayout.childCount) {
            if ((phrasesLayout.getChildAt(i) as BeamPhraseInput).phraseView.text.isNullOrBlank()) {
                return false
            }
        }

        return true
    }

    override fun clearListeners() {
        btnRestore.setOnClickListener(null)
    }

    override fun clearWindowState() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun initPresenter(): BasePresenter<out MvpView, out MvpRepository> {
        presenter = WelcomeRestorePresenter(this, WelcomeRestoreRepository(), WelcomeRestoreState())
        return presenter
    }

    interface WelcomeRestoreHandler {
        fun proceedToPasswords(phrases: Array<String>)
    }
}
