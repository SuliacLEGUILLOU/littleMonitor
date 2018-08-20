package s18alg.myapplication.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.target_cell.view.*
import s18alg.myapplication.R
import s18alg.myapplication.model.TargetWebsite

class TargetAdapter(var Targets: List<TargetWebsite>, var context: Context): BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val row:View = layoutInflater.inflate(R.layout.target_cell, viewGroup, false)

        row.target_name.text = Targets[position].uri
        row.target_ping.text = String.format("%.1fms", Targets[position].average_delay)

        row.target_uptime.text = when (Targets[position].tryNumber > 0) {
            true -> String.format("%d%%", Targets[position].pingSuccess * 100 / Targets[position].tryNumber)
            false -> "No uptime information yet"
        }
        if (!Targets[position].isUriValide) {
            row.target_name.setTextColor(Color.parseColor("#dd2222"))
        }
        return row
    }

    override fun getItem(position: Int): Any {
        return Targets[position]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return Targets.size
    }


}