package top.ourfor.app.iPlayClient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import top.ourfor.app.iPlayClient.databinding.PlayerBinding
import top.ourfor.app.iPlayClient.mpv.MPVView


@SuppressLint("ResourceType")
class PlayerView(
    context: Context,
    url: String?
) : ConstraintLayout(context) {
    init {
        setBackgroundColor(Color.BLUE)

        val binding = PlayerBinding.inflate(LayoutInflater.from(context))
        val player = binding.player
        val contentLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentLayoutParams.topToTop = LayoutParams.PARENT_ID;
        contentLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
        contentLayoutParams.leftToLeft = LayoutParams.PARENT_ID;
        contentLayoutParams.rightToRight = LayoutParams.PARENT_ID;
        addView(binding.root, contentLayoutParams)

        if (url != null) player.playFile(url)
        player.initialize(context.filesDir.path, context.cacheDir.path)


        val controlView = PlayerControlView(context)
        val controlLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        controlLayoutParams.topToTop = LayoutParams.PARENT_ID;
        controlLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
        controlLayoutParams.leftToLeft = LayoutParams.PARENT_ID;
        controlLayoutParams.rightToRight = LayoutParams.PARENT_ID;
        addView(controlView, controlLayoutParams)
    }
}