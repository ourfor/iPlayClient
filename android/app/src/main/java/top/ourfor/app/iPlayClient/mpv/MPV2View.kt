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
    }

    private var filePath: String? = null

    // Called when back button is pressed, or app is shutting down
    fun destroy() {
        holder.removeCallback(this)
        MPVLib.destroy()
    }


    // Surface callbacks

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        viewModel.resize("${width}x$height")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.w(TAG, "attaching surface")
        viewModel.attach(holder)
        if (filePath != null) {
            viewModel.loadVideo(filePath)
            viewModel.play()
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
        private const val TAG = "MPV2View"
    }
}
