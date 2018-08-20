package s18alg.myapplication.presenter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_target_detail.*
import s18alg.myapplication.R
import s18alg.myapplication.model.TargetWebsite

class TargetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_detail)

        val target = intent.getParcelableExtra<TargetWebsite>("Target")

        target_detail_name.text = target.uri
        target_detail_ping.text = String.format("%.1fms", target.average_delay)
        target_detail_code.text = target.returnCode.toString()
        target_detail_uptime.text = when (target.tryNumber > 0) {
            true -> String.format("%d%%", target.pingSuccess * 100 / target.tryNumber)
            false -> "No uptime information yet"
        }
    }
}
