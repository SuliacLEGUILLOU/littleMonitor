package s18alg.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_target_description.*
import s18alg.myapplication.R

class TargetDescriptionActivity : AppCompatActivity() {
    companion object {
        val NEW_TARGET_DESCRIPTION = "new target"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_description)

        doneButton.setOnClickListener { view -> doneClicked(view) }
    }

    private fun doneClicked(view: View) {
        val targetDescription = urlEdit.text.toString()

        if (!targetDescription.isEmpty()) {
            val result = Intent()
            result.putExtra(NEW_TARGET_DESCRIPTION, targetDescription)
            setResult(Activity.RESULT_OK, result)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}
