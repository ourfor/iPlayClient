package top.ourfor.lib.mpv;

import android.util.Log;
import android.view.Surface;

public class MPV {
    private static String TAG = "mpv";
    private long handle = 0L;

    static {
        String[] libs = { "mpv", "iPlayClient" };
        for (String lib: libs) {
            System.loadLibrary(lib);
        }
    }

    public MPV() {
        if (handle == 0) {
            Log.d(TAG, "init mpv");
            init();
        }
    }

    public native void init();
    public native void setDrawable(Surface surface);
    public native void command(String... args);
    public native void destroy();
    public native boolean getBoolTypeProperty(String name);
    public native void setBoolTypeProperty(String name, boolean flag);
    public native void setStringTypeProperty(String name, String value);

    public void Hello() {
        init();
    }
}
