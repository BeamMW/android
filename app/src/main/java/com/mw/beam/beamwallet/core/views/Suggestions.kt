package com.mw.beam.beamwallet.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mw.beam.beamwallet.R
import kotlinx.android.synthetic.main.suggestion_layout.view.*


class Suggestions: LinearLayout {
    private var suggestions: List<String>? = null
    private var onSuggestionClick: OnSuggestionClick? = null
    var mode: SuggestionsMode = SuggestionsMode.Default

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.suggestion_layout, this)

        leftWord.setOnClickListener(::onClick)
        centerWord.setOnClickListener(::onClick)
        rightWord.setOnClickListener(::onClick)
    }

    fun setSuggestions(suggestions: List<String>) {
        this.suggestions = suggestions
    }

    fun clear() {
        leftWord.text = ""
        centerWord.text = ""
        rightWord.text = ""

        updateDividers()
    }

    fun find(text: String) {
        clear()

        if (text.isEmpty()) {
            return
        }

        val words = suggestions?.filter { it.startsWith(text) }?.take(3)

        if (mode == SuggestionsMode.SingleWord && words?.size ?: 0 > 1) {
            updateDividers()
            return
        }

        words?.forEachIndexed { index, word ->
            val textView = when (index) {
                1 -> leftWord
                2 -> rightWord
                else -> centerWord
            }

            textView.text = word
        }

        updateDividers()
    }

    fun contains(text: String): Boolean = suggestions?.contains(text) ?: false

    private fun updateDividers() {
        dividerLeft.visibility = if (leftWord.text.isEmpty())  View.GONE else View.VISIBLE
        dividerRight.visibility = if (rightWord.text.isEmpty())  View.GONE else View.VISIBLE
    }

    fun setOnSuggestionClick(onSuggestionClick: OnSuggestionClick?) {
        this.onSuggestionClick = onSuggestionClick
    }

    private fun onClick(v: View?) {
        if (v != null && v is TextView && v.text.isNotEmpty()) {
            onSuggestionClick?.onClick(v.text.toString())
        }
    }

    enum class SuggestionsMode {
        SingleWord, Default
    }
}

interface OnSuggestionClick {
    fun onClick(suggestion: String)
}