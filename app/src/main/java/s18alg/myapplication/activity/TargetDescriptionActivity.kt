package s18alg.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_target_description.*
import s18alg.myapplication.R
import s18alg.myapplication.model.Profile

class TargetDescriptionActivity : AppCompatActivity() {
    companion object {
        val NEW_TARGET_URI = "new target uri"
        val NEW_TARGET_PROFILE = "new target profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_description)

        val item: Array<String> = arrayOf(Profile.Personal.name, Profile.Professional.name, Profile.Other.name)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item)
        profileEdit.adapter = adapter

        doneButton.setOnClickListener { view -> doneClicked(view) }
    }

    private fun doneClicked(view: View) {
        val targetDescription = urlEdit.text.toString()
        val targetProfile = profileEdit.selectedItemPosition

        if (!targetDescription.isEmpty()) {
            val result = Intent()
            result.putExtra(NEW_TARGET_URI, targetDescription)
            result.putExtra(NEW_TARGET_PROFILE, targetProfile)
            setResult(Activity.RESULT_OK, result)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}
