package top.ourfor.app.iPlayClient.mpv

import android.content.Context
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import top.ourfor.app.iPlayClient.Player
import top.ourfor.app.iPlayClient.PlayerViewModel
import top.ourfor.app.iPlayClient.R
import top.ourfor.lib.mpv.MPV
import top.ourfor.lib.mpv.MPVLib
import top.ourfor.lib.mpv.MPVLib.mpvFormat.MPV_FORMAT_DOUBLE
import top.ourfor.lib.mpv.MPVLib.mpvFormat.MPV_FORMAT_FLAG
import top.ourfor.lib.mpv.MPVLib.mpvFormat.MPV_FORMAT_INT64
import top.ourfor.lib.mpv.MPVLib.mpvFormat.MPV_FORMAT_NONE
import top.ourfor.lib.mpv.MPVLib.mpvFormat.MPV_FORMAT_STRING
import kotlin.reflect.KProperty

class MPV2View(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    public lateinit var viewModel: Player
    fun initialize(configDir: String, cacheDir: String) {
        viewModel = PlayerViewModel()
        holder.addCallback(this)
        observeProperties()
    }

    private var voInUse: String = ""

    private var filePath: String? = null

    fun playFile(url: String) {
        viewModel.loadVideo(url)
    }
    // Called when back button is pressed, or app is shutting down
    fun destroy() {
        holder.removeCallback(this)
        MPVLib.destroy()
    }

    private fun observeProperties() {
        // This observes all properties needed by MPVView, MPVActivity or other classes
        data class Property(val name: String, val format: Int = MPV_FORMAT_NONE)
        val p = arrayOf(
            Property("time-pos", MPV_FORMAT_INT64),
            Property("duration", MPV_FORMAT_INT64),
            Property("pause", MPV_FORMAT_FLAG),
            Property("paused-for-cache", MPV_FORMAT_FLAG),
            Property("track-list"),
            // observing double properties is not hooked up in the JNI code, but doing this
            // will restrict updates to when it actually changes
            Property("video-params/aspect", MPV_FORMAT_DOUBLE),
            //
            Property("playlist-pos", MPV_FORMAT_INT64),
            Property("playlist-count", MPV_FORMAT_INT64),
            Property("video-format"),
            Property("media-title", MPV_FORMAT_STRING),
            Property("metadata/by-key/Artist", MPV_FORMAT_STRING),
            Property("metadata/by-key/Album", MPV_FORMAT_STRING),
            Property("loop-playlist"),
            Property("loop-file"),
            Property("shuffle", MPV_FORMAT_FLAG),
            Property("hwdec-current")
        )

        for ((name, format) in p)
            MPVLib.observeProperty(name, format)
    }


    // Surface callbacks

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//        MPVLib.setPropertyString("android-surface-size", "${width}x$height")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.w(TAG, "attaching surface")
        viewModel.attach(holder)
        if (filePath != null) {
            viewModel.loadVideo(filePath)
            filePath = null
        } else {
            // We disable video output when the context disappears, enable it back
//            MPVLib.setPropertyString("vo", voInUse)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.w(TAG, "detaching surface")
        viewModel.destroy();
    }

    companion object {
        private const val TAG = "mpv"
    }
}
