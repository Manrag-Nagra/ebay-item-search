package com.example.ebayitemsearch.apiservice

import com.example.ebayitemsearch.models.Item
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "Accept: application/json",
        "Authorization:Bearer <API KEY HERE>",
        "Content-Type: application/json",
        "X-EBAY-C-MARKETPLACE-ID: EBAY_CA"
    )
    @GET("buy/browse/v1/item_summary/search")
    fun fetchEbayItem(@Query("q") item: String, @Query("offset") offsetNum: Int, @Query("filter") filterName: String): Call<Item>

}