package top.ourfor.lib.mpv;

public class MPV {
    private long handle;

    static {
        String[] libs = { "mpv", "iPlayClient" };
        for (String lib: libs) {
            System.loadLibrary(lib);
        }
    }

    public static native void init();
    public static native boolean getBoolTypeProperty(String name);

    public void Hello() {
        init();
    }
}
