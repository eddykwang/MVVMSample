package com.eddystudio.mvvmsample.service

import com.eddystudio.mvvmsample.model.ProductItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

private const val API_CATEGORY_BASE_PATH = ""

interface ProductService {
  @GET("$API_CATEGORY_BASE_PATH/category_{category}.json")
  fun getItemByCategory(@Path("category") category: String): Call<List<ProductItem>>
}