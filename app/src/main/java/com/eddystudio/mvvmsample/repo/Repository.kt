package com.eddystudio.mvvmsample.repo

import com.eddystudio.mvvmsample.model.ProductItem
import com.eddystudio.mvvmsample.service.ProductService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

  var productService: ProductService

  init {

    val retrofit = Retrofit.Builder()
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://pevmmwdjb.bkt.gdipper.com")
        .build()

    productService = retrofit.create(ProductService::class.java)
  }


  fun getProduct(cata: String): Observable<List<ProductItem>?>? {
    return Observable.fromCallable { productService.getItemByCategory(cata).execute() }
        .map { it.body() }
        .subscribeOn(Schedulers.io())
  }
}