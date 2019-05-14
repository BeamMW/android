package com.mw.beam.beamwallet.screens.address_edit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.core.helpers.Category
import com.mw.beam.beamwallet.core.helpers.CategoryHelper
import com.mw.beam.beamwallet.core.views.CategoryItemView

class CategoryAdapter(context: Context, private val categories: List<Category>) : ArrayAdapter<Category>(context, R.layout.item_category, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCategoryView(convertView, position, true)
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createCategoryView(convertView, position, false)

        val offset = context.resources.getDimensionPixelSize(R.dimen.settings_common_offset)
        view.setPadding(offset, offset / 2, offset, offset / 2)
        return view
    }

    private fun createCategoryView(convertView: View?, position: Int, hideCircleViewNoneCategory: Boolean): View {
        val view = convertView ?: CategoryItemView(context)
        val category = categories[position]
        val isNoneCategory = category.id == CategoryHelper.noneCategory.id

        (view as CategoryItemView).apply {
            colorResId = if (isNoneCategory) android.R.color.transparent else category.color.getAndroidColorId()
            text = if (isNoneCategory) context.getString(R.string.edit_address_category_none_name) else category.name
        }

        val circleVisibility = if (isNoneCategory && hideCircleViewNoneCategory) View.GONE else View.VISIBLE

        view.findViewById<View>(R.id.colorCircle).visibility = circleVisibility
        view.findViewById<View>(R.id.emptySpaceDivider).visibility = circleVisibility

        return view
    }
}