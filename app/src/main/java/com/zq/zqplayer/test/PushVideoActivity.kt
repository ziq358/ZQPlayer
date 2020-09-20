package com.zq.zqplayer.test

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.zq.playerlib.ZQPusher
import com.zq.zqplayer.R
import com.zq.zqplayer.common.Constants
import com.zq.zqplayer.util.VideoDataUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PushVideoActivity: AppCompatActivity() {

    lateinit var fileEditText: EditText
    lateinit var pushUrlEditText: EditText
    lateinit var startBtn: Button

    var pusher : ZQPusher = ZQPusher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_video)
        fileEditText = findViewById<EditText>(R.id.input_file)
        pushUrlEditText = findViewById<EditText>(R.id.push_url)
        startBtn = findViewById<Button>(R.id.start)
        fileEditText.setText(VideoDataUtil.getTestVideoPath(application))
        pushUrlEditText.setText(Constants.pushUrl)
        startBtn.setOnClickListener {
            GlobalScope.launch {
                pusher.pushVideo(fileEditText.text.toString(), pushUrlEditText.text.toString())
            }
        }
    }
}