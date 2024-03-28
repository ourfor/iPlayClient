package top.ourfor.app.iPlayClient;

import static top.ourfor.lib.mpv.MPV.MPV_EVENT_PROPERTY_CHANGE;
import static top.ourfor.lib.mpv.MPV.MPV_EVENT_SHUTDOWN;
import static top.ourfor.lib.mpv.MPV.MPV_FORMAT_DOUBLE;
import static top.ourfor.lib.mpv.MPV.MPV_FORMAT_FLAG;

import android.util.Log;
import android.view.SurfaceHolder;

import top.ourfor.lib.mpv.MPV;

public class PlayerViewModel implements Player {
    public PlayerEventListener delegate;
    public Thread eventLoop;

    public String url = null;
    private MPV mpv;
    public PlayerViewModel(String configDir, String cacheDir) {
        mpv = new MPV();
        mpv.create();
        mpv.setOptionString("profile", "fast");
        mpv.setOptionString("vo", "gpu-next");
        mpv.setOptionString("gpu-context", "android");
        mpv.setOptionString("opengl-es", "yes");
        mpv.setOptionString("hwdec", "auto");
        mpv.setOptionString("hwdec-codecs", "h264,hevc,mpeg4,mpeg2video,vp8,vp9,av1");
        mpv.setOptionString("ao", "audiotrack,opensles");
        mpv.setOptionString("config", "yes");
        mpv.setOptionString("config-dir", configDir);
        mpv.setOptionString("gpu-shader-cache-dir", cacheDir);
        mpv.setOptionString("icc-cache-dir", cacheDir);
        mpv.init();

        watch();
    }

    @Override
    public void setDelegate(PlayerEventListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setVideoOutput(String value) {
        mpv.setStringProperty("vo", value);
    }

    public void attach(SurfaceHolder holder) {
        mpv.setDrawable(holder.getSurface());
    }

    @Override
    public void detach() {
        mpv.setStringProperty("vo", "null");
        mpv.setOptionString("force-window", "no");
        mpv.setDrawable(null);
    }

    @Override
    public void loadVideo(String url) {
        mpv.command("loadfile", url);
    }

    @Override
    public void resize(String newSize) {
        mpv.setStringProperty("android-surface-size", newSize);
    }

    @Override
    public void seek(long timeInSeconds) {
        mpv.command("seek", String.valueOf(timeInSeconds), "absolute+keyframes");
    }

    @Override
    public boolean isPlaying() {
        return !(mpv.getBoolProperty("pause"));
    }

    @Override
    public void resume() {
        mpv.setBoolProperty("pause", false);
    }

    @Override
    public void pause() {
        mpv.setBoolProperty("pause", true);
    }

    @Override
    public void stop() {
        mpv.command("stop");
    }

    @Override
    public void destroy() {
        mpv.destroy();
    }

    public void watch() {
        if (eventLoop ==  null) {
            mpv.observeProperty(0, "time-pos", MPV.MPV_FORMAT_DOUBLE);
            mpv.observeProperty(0, "duration", MPV.MPV_FORMAT_DOUBLE);
            mpv.observeProperty(0, "paused-for-cache", MPV.MPV_FORMAT_FLAG);
            mpv.observeProperty(0, "paused", MPV.MPV_FORMAT_FLAG);
            eventLoop = new Thread(() -> {
                while (true) {
                    MPV.Event e = mpv.waitEvent(-1);
                    if (e.type == MPV_EVENT_SHUTDOWN) {
                        Log.d(TAG, "close mpv player");
                        break;
                    }

                    if (e.type == MPV_EVENT_PROPERTY_CHANGE) {
                        if (delegate == null) return;
                        Object value = null;
                        if (e.format == MPV_FORMAT_DOUBLE) {
                            value = mpv.getDoubleProperty(e.prop);
                        } else if (e.format == MPV_FORMAT_FLAG) {
                            value = mpv.getBoolProperty(e.prop);
                        }
                        delegate.onPropertyChange(e.prop, value);
                    }
                }
            });
        }
        eventLoop.start();
    }

    public void unwatch() {
        if (eventLoop != null) {
            eventLoop.stop();
        }
    }

    static String TAG = "PlayerViewModel";
}
