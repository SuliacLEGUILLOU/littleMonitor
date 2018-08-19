package s18alg.myapplication.activity

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import s18alg.myapplication.R
import s18alg.myapplication.model.AppDatabase
import s18alg.myapplication.model.SingletonHolder
import s18alg.myapplication.model.TargetWebsite
import s18alg.myapplication.presenter.TargetAdapter
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {

    companion object: SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it, AppDatabase::class.java, "db-monitor").build()
    })

    private val targetList: MutableList<TargetWebsite> = mutableListOf()
    private val adapter by lazy { makeAdapter() }
    private val ADD_TARGET_REQUEST = 1
    private val client = OkHttpClient()
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        targetListView.adapter = adapter
        targetListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> itemSelect(position) }

        addTargetButton.setOnClickListener { view -> addTargetClicked(view) }

        launch {
            targetList += getInstance(applicationContext).targetDao().getAll()
            adapter.notifyDataSetChanged()
        }

        autoRefreshService()
        targetsScanService()
        dataSaveService()
    }

    private fun targetsScanService() {
        timer.scheduleAtFixedRate(10000, 10000) {
            Log.i("DATA", "Scanning target")
            targetList.forEach {
                try {
                    val req = Request.Builder().url(it.uri).build()
                    val timeStart = Calendar.getInstance().timeInMillis

                    client.newCall(req).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            it.updatePing(false)
                        }
                        override fun onResponse(call: Call, response: Response) {
                            // Set last code
                            if (response.isSuccessful) {
                                it.updatePing(true, (Calendar.getInstance().timeInMillis - timeStart) * 1.0)
                            } else {
                                it.updatePing(false)
                            }
                        }
                    })
                } catch (e: IllegalArgumentException) {
                    // Mark the url as error here.
                    it.updatePing(false)
                }
            }
        }
    }

    private fun dataSaveService() {
        timer.scheduleAtFixedRate(14000, 10000) {
            Log.i("DATA", "Starting to save data")
            targetList.forEach {
                getInstance(applicationContext).targetDao().update(it)
            }
            Log.i("DATA", "data saved")
        }

    }

    private fun autoRefreshService() {
        val refreshTask = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            Log.i("DATA", "Refreshing main view")
            adapter.notifyDataSetChanged()
        }

        timer.scheduleAtFixedRate(15000, 10000) {
            refreshTask.post(runnable)
        }
    }

    override fun onStop() {
        super.onStop()
        launch {
            targetList.forEach {
                getInstance(applicationContext).targetDao().update(it)
            }
            Log.i("DATA", "data saved")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun itemSelect(position: Int) {
        Log.d("Event", String.format("select elem %d", position))
    }

    private fun addTargetClicked(view: View) {
        val intent = Intent(this, TargetDescriptionActivity::class.java)
        startActivityForResult(intent, ADD_TARGET_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TARGET_REQUEST && resultCode == Activity.RESULT_OK) {
            val target = data?.getStringExtra(TargetDescriptionActivity.NEW_TARGET_DESCRIPTION)
            target?.let {
                val newTarget = TargetWebsite(target)
                targetList.add(newTarget)
                adapter.notifyDataSetChanged()
                launch {
                    getInstance(applicationContext).targetDao().insert(newTarget)
                }
            }
        }
    }

    private fun makeAdapter(): TargetAdapter =
            TargetAdapter(targetList, applicationContext)
}
