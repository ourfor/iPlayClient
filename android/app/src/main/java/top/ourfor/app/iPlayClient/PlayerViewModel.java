package top.ourfor.app.iPlayClient;

import android.content.Context;
import android.view.SurfaceHolder;

import java.util.List;

import top.ourfor.lib.mpv.MPVLib;

public class PlayerViewModel implements Player {
    public void attach(SurfaceHolder holder) {
        MPVLib.attachSurface(holder.getSurface());
        MPVLib.setOptionString("force-window", "yes");
    }

    @Override
    public void loadVideo(String url) {
        MPVLib.command(new String[]{"loadfile", url});
    }

    @Override
    public void resume() {
        MPVLib.setPropertyBoolean("pause", false);
    }

    @Override
    public boolean isPlaying() {
        return !MPVLib.getPropertyBoolean("pause");
    }

    @Override
    public void pause() {
        MPVLib.setPropertyBoolean("pause", true);
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        MPVLib.setPropertyString("vo", "null");
        MPVLib.setOptionString("force-window", "no");
        MPVLib.detachSurface();
    }
}
