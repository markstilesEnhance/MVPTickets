package com.example.tickets_with_coroutines.view

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets_with_coroutines.R
import com.example.tickets_with_coroutines.network.Repository
import com.example.tickets_with_coroutines.network.model.Ticket
import com.example.tickets_with_coroutines.network.ApiClient.getClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val from = "DEL"
    private val to = "HYD"
    lateinit var mAdapter: TicketsAdapter
    private var ticketsList: MutableList<Ticket> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    private val parentJob = Job()
    lateinit var ticket: Ticket
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val repository:Repository = Repository(getClient)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "$from > $to"
        initRecyclerView()

        scope.launch {
            ticketsList = repository.tickets(from, to)!!
            mAdapter.updateTickets(ticketsList)

            for (tix in ticketsList) {
                tix.price = repository.prices(tix.flightNumber, from, to)!!
                val position = ticketsList.indexOf(tix)
                mAdapter.updatePrice(tix, position)
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = TicketsAdapter(applicationContext, ticketsList){ ticket : Ticket -> onTicketSelected(ticket) }
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(1, dpToPx(5), true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter
    }

    private fun showError() {
        Log.e("", "showError: ")
    }

    override fun onPause() {
        super.onPause()
        coroutineContext.cancel()
    }

    private fun dpToPx(dp: Int): Int {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).roundToInt()
    }

    private fun onTicketSelected(ticket: Ticket?) {
        Toast.makeText(this, "Clicked: ${ticket?.flightNumber}", Toast.LENGTH_LONG).show()
    }

}