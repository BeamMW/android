package com.mw.beam.beamwallet.screens.category

import com.mw.beam.beamwallet.base_screen.BasePresenter
import com.mw.beam.beamwallet.core.entities.WalletAddress
import io.reactivex.disposables.Disposable

class CategoryPresenter(view: CategoryContract.View?, repository: CategoryContract.Repository, private val state: CategoryState)
    : BasePresenter<CategoryContract.View, CategoryContract.Repository>(view, repository), CategoryContract.Presenter {
    private lateinit var addressesSubscription: Disposable

    override fun onStart() {
        super.onStart()
        state.category = repository.getCategoryFromId(view?.getCategoryId() ?: "")

        state.category?.let {
            view?.init(it)

            val addresses = state.addresses.filter { address -> it.addresses.contains(address.walletID) }

            state.setAddresses(addresses)
            view?.updateAddresses(addresses)
        }
    }

    override fun initSubscriptions() {
        super.initSubscriptions()

        addressesSubscription = repository.getAddresses().subscribe {
            val list = it.addresses?.filter { walletAddress ->
                state.category?.addresses?.contains(walletAddress.walletID) ?: false
            }

            if (list != null) {
                state.addAddresses(list)
            }

            view?.updateAddresses(state.addresses)
        }
    }

    override fun onAddressPressed(address: WalletAddress) {
        view?.showAddressDetails(address)
    }

    override fun getSubscriptions(): Array<Disposable>? = arrayOf(addressesSubscription)

    override fun onEditCategoryPressed() {
        state.category?.let { view?.navigateToEditCategory(it.id) }
    }

    override fun onDeleteCategoryPressed() {
        state.category?.let { repository.deleteCategory(it) }
        view?.finish()
    }
}