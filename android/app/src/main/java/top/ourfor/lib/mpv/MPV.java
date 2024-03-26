package top.ourfor.lib.mpv;

import android.view.Surface;

public class MPV {
    private long handle = 0L;

    static {
        String[] libs = { "mpv", "iPlayClient" };
        for (String lib: libs) {
            System.loadLibrary(lib);
        }
    }

    public MPV() {
        if (handle == 0) init();
    }

    public native void init();
    public native void setDrawable(Surface surface);
    public native void command(String... args);
    public native void destroy();
    public native boolean getBoolTypeProperty(String name);

    public void Hello() {
        init();
    }
}
