package com.eddystudio.mvvmsample.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eddystudio.mvvmsample.model.ItemStatus
import com.eddystudio.mvvmsample.model.ProductItem

class ItemVM(val productItem: ProductItem) {
  val imageUrl: MutableLiveData<String> = MutableLiveData()
  val showBadge: MutableLiveData<Boolean> = MutableLiveData()
  val title: MutableLiveData<String> = MutableLiveData()
  val rate: MutableLiveData<String> = MutableLiveData()
  val comment: MutableLiveData<String> = MutableLiveData()
  val price: MutableLiveData<String> = MutableLiveData()

  init {
    imageUrl.postValue(productItem.imgUrl)
    showBadge.postValue(productItem.status == ItemStatus.SOLD_OUT)
    title.postValue(productItem.name)
    rate.postValue(productItem.likes.toString())
    comment.postValue(productItem.comments.toString())
    price.postValue(productItem.price.toString())
  }
}