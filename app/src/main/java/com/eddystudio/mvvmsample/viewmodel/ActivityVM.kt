package com.eddystudio.mvvmsample.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eddystudio.mvvmsample.model.ProductItem
import com.eddystudio.mvvmsample.repo.Repository
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class ActivityVM : ViewModel() {
  private val eventSubject = PublishSubject.create<Event>()

  val showLoading = MutableLiveData<Boolean>()

  init {
    showLoading.postValue(true)
  }

  fun getEvent(): Observable<Event> {
    return eventSubject.hide()
  }

  private val compositeDisposable = CompositeDisposable()
  fun getData() {
    val repo = Repository()
    showLoading.postValue(true)
    compositeDisposable.add(
        repo.getProduct("all")
            ?.map { toVm(it) }
            ?.subscribe(
                {
                  eventSubject.onNext(Event.OnCompeleted(it!!))
                }
                ,
                {
                  eventSubject.onNext(Event.OnError(it))
                }
                , {
              showLoading.postValue(false)
            }
            )!!
    )
  }

  private fun toVm(it: List<ProductItem>): List<ItemVM> {
    val list = ArrayList<ItemVM>()
    for(i in it) {
      list.add(ItemVM(i))
    }
    return list
  }


  fun onVmCleared() {
    compositeDisposable.clear()
  }


  interface Event {
    data class OnCompeleted(val list: List<ItemVM>) : Event
    data class OnError(val throwable: Throwable) : Event
  }

}

@BindingAdapter("bind:imageUrl")
fun ImageView.setImageUrl(imageUrl: String) {
  Picasso.get()
      .load(imageUrl)
      .into(this)
}