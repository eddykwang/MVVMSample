package com.eddystudio.mvvmsample.model

import com.google.gson.annotations.SerializedName

data class ProductItem(

    @SerializedName("id")
    val itemId: String,
    val name: String,
    var categoryName: String? = null,
    var status: ItemStatus = ItemStatus.ON_SALE,

    @SerializedName("num_likes")
    var likes: Int = 0,

    @SerializedName("num_comments")
    var comments: Int = 0,
    var price: Double = 0.0,

    @SerializedName("photo")
    var imgUrl: String? = null
)

enum class ItemStatus {
  ON_SALE, SOLD_OUT
}