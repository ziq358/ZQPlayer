package com.zq.zqplayer.util

import android.app.Application
import android.content.Context
import com.zq.zqplayer.R
import java.io.*

class VideoDataUtil {

    companion object{
        fun getTestVideoPath(context: Application):String {
            var videoPath: String = getDataDirPath(context, "meidacodec") + File.separator + "gao_bai_qi_qiu.mp4"
            val file = File(videoPath)
            if (file.exists()) {
                return videoPath
            }
            val `in` = BufferedInputStream(context.resources.openRawResource(R.raw.gao_bai_qi_qiu))
            val out: BufferedOutputStream
            try {
                val outputStream = FileOutputStream(videoPath)
                out = BufferedOutputStream(outputStream)
                val buf = ByteArray(1024)
                var size = `in`.read(buf)
                while (size > 0) {
                    out.write(buf, 0, size)
                    size = `in`.read(buf)
                }
                `in`.close()
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return videoPath
        }

        fun getDataDirPath(context: Context, dir: String): String {
            val path = context.externalCacheDir!!.absolutePath + File.separator + dir
            val file = File(path)
            if (!file.exists()) {
                file.mkdir()
            }
            return path
        }
    }
}