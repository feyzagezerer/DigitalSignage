package com.fey.signage.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.fey.signage.GenerateUUID
import com.fey.signage.R
import com.fey.signage.databinding.ActivityStandbyBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
@AndroidEntryPoint
class StandbyActivity : AppCompatActivity() {

    private val viewModel: StandbyViewModel by viewModels()
    private lateinit var binding: ActivityStandbyBinding

    private lateinit var  sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standby)

        binding = DataBindingUtil.setContentView<ActivityStandbyBinding?>(this, R.layout.activity_standby)
            .apply {
                lifecycleOwner = this@StandbyActivity
                viewModel = this@StandbyActivity.viewModel
            }

        checkFirstTime()
    }
    fun checkFirstTime() {
        val sharedPref = getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("PREF_FIRST_TIME", true)
        if (isFirstTime) {
            val uuid = GenerateUUID.generateSevenDigitAlphanumericUUID()
            GenerateUUID.assignmentUUID()
            Timber.tag("Check Uuid").e("UUID ${uuid.value}")
            val editor = sharedPref.edit()
            editor.putBoolean("PREF_FIRST_TIME", false)
            editor.putString("PREF_FIRST_UUID", uuid.value)
            editor.apply()


            viewModel.setup(uuid)
            if(viewModel.screenCreated()){
                val intent = Intent(this@StandbyActivity, MainActivity::class.java)

                val uuid =  sharedPref.getString("PREF_FIRST_UUID","")
                intent.putExtra("PREF_FIRST_UUID", uuid)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }

        }
        else{
            val uuid =  sharedPref.getString("PREF_FIRST_UUID","")
            val intent = Intent(this@StandbyActivity, MainActivity::class.java)

            intent.putExtra("uuid", uuid)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}