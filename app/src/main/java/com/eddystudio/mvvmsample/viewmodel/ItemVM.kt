package com.eddystudio.mvvmsample.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eddystudio.mvvmsample.model.ItemStatus
import com.eddystudio.mvvmsample.model.ProductItem

class ItemVM(val productItem: ProductItem) {
  val showBadge: MutableLiveData<Boolean> = MutableLiveData()
  val item = MutableLiveData<ProductItem>()

  init {
    showBadge.postValue(productItem.status?.toUpperCase() == ItemStatus.SOLD_OUT.toString())
    item.postValue(productItem)
  }
}