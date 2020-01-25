package com.example.ebayitemsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ebayitemsearch.adapter.ebay_item_adapter
import com.example.ebayitemsearch.apiservice.ApiService
import com.example.ebayitemsearch.models.Item
import com.example.ebayitemsearch.models.ItemSummary
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    var isLoading = false
    var listofItems: MutableList<ItemSummary> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton.setOnClickListener {
            fetchJSON()
        }
    }

    fun fetchJSON(){

        val url = "https://api.ebay.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val api = retrofit.create(ApiService::class.java)

        //fetch items
        api.fetchEbayItem(itemSearchText.text.toString()).enqueue(object: Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                Log.d("Retorfit", response.body().toString())

                val responseJson = response.body()

                //check to see if response is null
                if(responseJson != null) {

                    //send data to adapter
                    listofItems = responseJson.itemSummaries
                    itemRecycleView.layoutManager = LinearLayoutManager(this@MainActivity)
                    itemRecycleView.adapter = ebay_item_adapter(listofItems)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.d("Retorfit", t.toString())
            }

        })


    }
}
