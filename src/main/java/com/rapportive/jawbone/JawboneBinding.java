package com.rapportive.jawbone;

import com.sun.jna.Native;

public class JawboneBinding {
    static {
        Native.register("wbxml2");
    }
}
