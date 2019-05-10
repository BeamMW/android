package com.mw.beam.beamwallet.core.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.core.App
import kotlinx.android.synthetic.main.color_selector.view.*

class ColorSelector: FrameLayout {
    private val backgroundDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(App.self, R.drawable.color_selector_background)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    var isSelectedColor: Boolean = false
        set(value) {
            field = value
            if (field) {
                colorSelectorBackground.background = backgroundDrawable
            } else {
                colorSelectorBackground.background = null
            }
            refreshDrawableState()
        }

    var colorResId: Int? = null
        set(value) {
            field = value ?: R.color.colorAccent

            colorSelectorCard.setCardBackgroundColor(ColorStateList.valueOf(field!!))
            refreshDrawableState()
        }

    private fun init(context: Context) {
        inflate(context, R.layout.color_selector, this)

        isSelectedColor = false
        colorResId = null
    }
}
