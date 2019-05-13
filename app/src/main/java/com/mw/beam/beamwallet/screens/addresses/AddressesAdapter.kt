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

package com.mw.beam.beamwallet.screens.addresses

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.core.entities.WalletAddress
import com.mw.beam.beamwallet.core.helpers.Category
import com.mw.beam.beamwallet.core.utils.CalendarUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_address.*

/**
 * Created by vain onnellinen on 2/28/19.
 */
class AddressesAdapter(private val context: Context, private val clickListener: OnItemClickListener, private val categoryProvider: ((address: String) -> Category?)? = null) :
        RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {
    private val multiplyColor = ContextCompat.getColor(context, R.color.wallet_adapter_multiply_color)
    private val notMultiplyColor = ContextCompat.getColor(context, R.color.wallet_adapter_not_multiply_color)
    private val expiredDate = context.getString(R.string.addresses_expired)
    private val expiresDate = context.getString(R.string.addresses_expires)
    private val expiresNever = context.getString(R.string.addresses_never)

    private var data: List<WalletAddress> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)).apply {
        this.containerView.setOnClickListener {
            clickListener.onItemClick(data[adapterPosition])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = data[position]
        val addressCategory = categoryProvider?.invoke(address.walletID)

        holder.apply {
            label.text = address.label
            id.text = address.walletID
            itemView.setBackgroundColor(if (position % 2 == 0)  notMultiplyColor else multiplyColor) //logically reversed because count starts from zero
            if (!address.isContact) {
                date.text = String.format(if (address.isExpired) expiredDate else expiresDate,
                        if (address.duration == 0L) expiresNever else CalendarUtils.fromTimestamp(address.createTime + address.duration))
            }
            category.visibility = if (addressCategory == null) View.GONE else View.VISIBLE
            category.text = addressCategory?.name ?: ""

            if (addressCategory != null) {
                category.setTextColor(context.resources.getColor(addressCategory.color.getAndroidColorId(), context.theme))
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: List<WalletAddress>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: WalletAddress)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
