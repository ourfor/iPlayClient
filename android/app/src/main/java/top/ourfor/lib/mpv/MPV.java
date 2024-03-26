package top.ourfor.lib.mpv;

public class MPV {
    private long handle;

    static {
        String[] libs = { "mpv", "iPlayClient" };
        for (String lib: libs) {
            System.loadLibrary(lib);
        }
    }

    public native void init();
    public native void destroy();
    public native boolean getBoolTypeProperty(String name);

    public void Hello() {
        init();
    }
}
