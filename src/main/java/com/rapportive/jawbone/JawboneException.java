package com.rapportive.jawbone;

import com.sun.jna.Native;

@SuppressWarnings("serial")
public class JawboneException extends RuntimeException {
    static {
        Native.register("wbxml2");
    }

    private static final String DEFAULT_PREFIX = "libwbxml2 error";

    private final String message;

    public JawboneException(int ret) {
        this(DEFAULT_PREFIX, ret);
    }

    public JawboneException(String prefix, int ret) {
        this.message = prefix + ": " + wbxml_errors_string(ret);
    }

    @Override
    public String getMessage() {
        return message;
    }


    private native String wbxml_errors_string(int ret);


    public static void check(int ret) {
        check(DEFAULT_PREFIX, ret);
    }

    public static void check(String prefix, int ret) {
        if (ret != 0) {
            throw new JawboneException(prefix, ret);
        }
    }
}
