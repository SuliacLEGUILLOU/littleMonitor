package s18alg.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_target_detail.*
import s18alg.myapplication.R
import s18alg.myapplication.formatPingText
import s18alg.myapplication.formatUptime
import s18alg.myapplication.model.TargetWebsite

class TargetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_detail)

        val target = intent.getParcelableExtra<TargetWebsite>("Target")

        target_detail_name.text = target.uri
        target_detail_code.text = String.format("Last return code : %d", target.returnCode)
        target_detail_ping.text = formatPingText(target)
        target_detail_uptime.text = formatUptime(target)
        target_detail_profile.text = String.format("Profile : %s", target.profile.name)
    }
}
