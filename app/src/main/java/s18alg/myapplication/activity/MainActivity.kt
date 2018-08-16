package s18alg.myapplication.activity

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.launch
import s18alg.myapplication.R
import s18alg.myapplication.model.AppDatabase
import s18alg.myapplication.model.SingletonHolder
import s18alg.myapplication.model.TargetWebsite
import s18alg.myapplication.presenter.TargetAdapter

class MainActivity : AppCompatActivity() {

    companion object: SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it, AppDatabase::class.java, "db-monitor").build()
    })

    private val targetList: MutableList<TargetWebsite> = mutableListOf()
    private val adapter by lazy { makeAdapter() }
    private val ADD_TARGET_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        targetListView.adapter = adapter
        targetListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.i("Event", String.format("select elem %d", position))
            targetList[position].select()
            adapter.notifyDataSetChanged()
        }

        addTargetButton.setOnClickListener { view -> addTargetClicked(view) }

        launch {
            targetList += getInstance(applicationContext).targetDao().getAll()
            adapter.notifyDataSetChanged()
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
