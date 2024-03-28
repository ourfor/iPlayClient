package top.ourfor.app.iPlayClient

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.facebook.react.uimanager.ThemedReactContext

@RequiresApi(Build.VERSION_CODES.O)
class PlayerFragment (
    var url: String?
) : Fragment() {
    private lateinit var playerView: PlayerView
    var themedReactContext: ThemedReactContext? = null
    var title: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        playerView = PlayerView(requireNotNull(context), url)
        playerView.title = title
        playerView.themedReactContext = themedReactContext
        return playerView // this CustomView could be any view that you want to render
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // do any logic that should happen in an `onCreate` method, e.g:
        Log.d("PlayerView", "onViewCreated")
    }

    override fun onPause() {
        super.onPause()
        // do any logic that should happen in an `onPause` method
        // e.g.: customView.onPause();
    }

    override fun onResume() {
        super.onResume()
        // do any logic that should happen in an `onResume` method
        // e.g.: customView.onResume();
    }

    override fun onDestroy() {
        super.onDestroy()
        // do any logic that should happen in an `onDestroy` method
        // e.g.: customView.onDestroy();
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        playerView.requestLayout()
    }
}