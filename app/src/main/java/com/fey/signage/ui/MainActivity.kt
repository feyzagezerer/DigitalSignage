package com.fey.signage.ui

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.fey.signage.R
import com.fey.signage.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
                viewModel = this@MainActivity.viewModel
            }

        initializePlayer()
        val uuidd = intent.getStringExtra("uuid")
    viewModel.setup(uuidd)
    }


    private fun initializePlayer() {
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        Timber.tag("Check namelist").e("name list mainac : %s", viewModel.namesList)
        viewModel.namesList.observe(this, Observer { names ->
            names?.forEach { name ->
                val imageUrl = "https://octopus-panel-case.azurewebsites.net/uploads/" + name
                Timber.tag("Check namelist").e("name list mainac name : %s", name)
                Timber.tag("Check namelist").e("imageUrl : %s", imageUrl)
               val cmd = "ffmpeg -loop 1 -t 3 -i " + name[0] + " -loop 1 -t 3 -i " +  name[0] + " -loop 1 -t 3 -i " +  name[0] + " -loop 1 -t 3 -i " +  name[0] + " -filter_complex [0:v]trim=duration=3,fade=t=out:st=2.5:d=0.5[v0];[1:v]trim=duration=3,fade=t=in:st=0:d=0.5,fade=t=out:st=2.5:d=0.5[v1];[2:v]trim=duration=3,fade=t=in:st=0:d=0.5,fade=t=out:st=2.5:d=0.5[v2];[3:v]trim=duration=3,fade=t=in:st=0:d=0.5,fade=t=out:st=2.5:d=0.5[v3];[v0][v1][v2][v3]concat=n=4:v=1:a=0,format=yuv420p[v] -map [v] -preset ultrafast "
                val cmdm = cmd +"/Users/ambeentmacbookpro/Signage/output.mp4"
                val process = Runtime.getRuntime().exec(cmdm)
                process.waitFor()
                val videoUri = Uri.parse("/Users/ambeentmacbookpro/Signage/output.mp4")
                val madiaItem = MediaItem.fromUri(videoUri)
                player?.addMediaItem(madiaItem)
                player?.prepare()
            }})
}


private fun stopPlayer() {
  player?.pause()

}

private fun releasePlayer() {
  player?.release()
  player = null
}
override fun onStart(){
super.onStart()
initializePlayer()

}
override fun onResume() {
  super.onResume()
  if(player==null) {
      initializePlayer()
  }
}

override fun onPause() {
  super.onPause()
  releasePlayer()
}

override fun onStop() {
  super.onStop()
  releasePlayer()
}

override fun onDestroy() {
  super.onDestroy()
  window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//  releasePlayer()

}
}