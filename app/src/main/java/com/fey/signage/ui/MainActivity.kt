package com.fey.signage.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.fey.signage.GenerateUUID
import com.fey.signage.GenerateUUID.uuid
import com.fey.signage.R
import com.fey.signage.api.VideoPlayerRepository
import com.fey.signage.databinding.ActivityMainBinding
import com.fey.signage.utils.Constants.BASE_URL
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var exoPlayer: ExoPlayer

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // var uuid = GenerateUUID.generateSevenDigitAlphanumericUUID()
        GenerateUUID.assignmentUUID()
        Timber.tag("Check Uuid").e("UUID ${uuid.value}")
       /* sharedPreferences = this.getSharedPreferences("STATE", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.putString("uuid", uuid.toString())
        editor.commit()*/

        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                viewModel = this@MainActivity.viewModel
            }


        setupViews(uuid)

    }

    @Suppress("DEPRECATION")
    private fun setupViews(uuid: MutableLiveData<String>) {
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        exoPlayer = ExoPlayer.Builder(this).build()
        binding.styledPlayerView.player = exoPlayer

        viewModel.setup(uuid)
    }
    fun setVideoUrl(uuid: String) {
        val videoUrl = BASE_URL + "screen/" + uuid
        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        startPlayer()
    }


    private fun startPlayer() {

        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    private fun stopPlayer() {
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    private fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        setVideoUrl("q4j3x1c")

    }

    override fun onPause() {
        super.onPause()
        stopPlayer()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        releasePlayer()

    }
}