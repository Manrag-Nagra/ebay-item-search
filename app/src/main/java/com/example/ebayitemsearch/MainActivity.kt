package com.example.ebayitemsearch

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    var isLoading = false
    var noMoreItems = false
    var listofItems: MutableList<ItemSummary> = ArrayList()
    var offset = 0
    var filter = ""

    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this@MainActivity)
        itemRecycleView.layoutManager = layoutManager



        //When user click on search
        searchButton.setOnClickListener {
            filter = ""
            offset = 0
            noMoreItems = false
            spinningIndicator.visibility = View.VISIBLE
            fetchJSON()
        }


        //If user scroll to bottom, fetch more item
        itemRecycleView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if(dy > 0) {
                    val visibleCount = layoutManager.childCount
                    val pastVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = itemRecycleView.adapter?.itemCount
                    if (!isLoading && !noMoreItems) {
                        //if at the end of the list
                        if ((visibleCount + pastVisible) >= total!!) {
                            offset += 50
                            loadMoreIndicator.visibility = View.VISIBLE
                            fetchJSON()
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        //When user clicks filter buttons
        freeShippingButton.setOnClickListener {
            filter = "maxDeliveryCost:0"
            offset = 0
            noMoreItems = false
            spinningIndicator.visibility = View.VISIBLE
            fetchJSON()
        }

    }


    //Fetch ebay item
    fun fetchJSON(){

        isLoading = true

        val url = "https://api.ebay.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val api = retrofit.create(ApiService::class.java)

        //fetch items
        api.fetchEbayItem(itemSearchText.text.toString(), offset, filter).enqueue(object: Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {

                loadMoreIndicator.visibility = View.GONE
                spinningIndicator.visibility = View.GONE

                Log.d("Retorfit", response.body().toString())

                val responseJson = response.body()

                //check to see if response is null
                if(responseJson != null) {

                    if(offset > 0 && responseJson.itemSummaries.last() != listofItems.last()){
                        listofItems.addAll((responseJson.itemSummaries))
                        itemRecycleView.adapter?.notifyDataSetChanged()
                        isLoading = false
                        return
                    } else if(offset > 0){
                        createToast("No more items")
                        isLoading = false
                        noMoreItems = true
                        return
                    }

                    //send data to adapter
                    listofItems = responseJson.itemSummaries
                    itemRecycleView.adapter = ebay_item_adapter(listofItems)
                }

                isLoading = false

            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.d("Retorfit", t.toString())
            }

        })


    }

    //Display data and send response to adapter
    private fun createToast(message: String) {
        //Create Toast
        val toast = Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_SHORT
        )
        //Change Toast background
        val view = toast.view
        view.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)

        toast.show()

    }
}
