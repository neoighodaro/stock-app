package com.example.stockexchangeapp


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.pushnotifications.PushNotifications
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

  private val mAdapter = StockListAdapter(ArrayList())
  private lateinit var sharedPreferences: SharedPreferences
  private val options = PusherOptions().setCluster("PUSHER_APP_CLUSTER")
  private val pusher = Pusher("PUSHER_APP_KEY", options)
  private val channel = pusher.subscribe("stock-channel")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupPrefs()
    pusher.connect()
    setupRecyclerView()
    setupPusherChannels()
    setupPushNotifications()
  }


  private fun setupPrefs() {
    PreferenceManager.setDefaultValues(this, R.xml.preference, false)
    sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this)
  }


  private fun setupRecyclerView() {
    with(recyclerView){
      layoutManager = LinearLayoutManager(this@MainActivity)
      adapter = mAdapter
      addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }
  }


  private fun setupPusherChannels(){

    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    MyStockList.stockList.forEachIndexed { index, element ->
      val refKey = element.name.toLowerCase() + "_preference"
      val refValue = sharedPref.getBoolean(refKey, false)
      if (refValue ){

        if (!mAdapter.contains(element)) {
          mAdapter.addItem(element)
          channel.bind(element.name) { channelName, eventName, data ->
            val jsonObject = JSONObject(data)
            runOnUiThread {
              mAdapter.updateItem(StockModel(eventName, jsonObject.getDouble("currentValue"), jsonObject.getDouble("changePercent")))
            }
          }
        }

      } else {
        mAdapter.removeItem(element.name)
        channel.unbind(element.name){ _, _, _ -> }
      }
    }

  }


  private fun setupPushNotifications() {
    PushNotifications.start(applicationContext,
        "PUSHER_BEAMS_INSTANCE_ID")
    PushNotifications.subscribe("stocks")
  }


  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.settings -> {
        startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  override fun onStart() {
    super.onStart()
    sharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
  }


  override fun onSharedPreferenceChanged(sharedPref: SharedPreferences?, key: String?) {
    setupPusherChannels()
  }

}
