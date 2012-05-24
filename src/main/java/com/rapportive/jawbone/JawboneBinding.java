package com.rapportive.jawbone;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class JawboneBinding {
    static {
        Native.register("wbxml2");
    }


    public native int wbxml_conv_wbxml2xml_create(PointerByReference convPtr);

    public native void wbxml_conv_wbxml2xml_set_gen_type(Pointer conv, int genType);
    public native void wbxml_conv_wbxml2xml_set_language(Pointer conv, int language);
    public native void wbxml_conv_wbxml2xml_set_charset(Pointer conv, int charset);
    public native void wbxml_conv_wbxml2xml_set_indent(Pointer conv, int indent);
    public native void wbxml_conv_wbxml2xml_enable_preserve_whitespaces(Pointer conv);

    public native int wbxml_conv_wbxml2xml_run(Pointer conv, byte[] wbxml, long wbxmlLength, PointerByReference xmlPtr, LongByReference xmlLength);

    public native void wbxml_conv_wbxml2xml_destroy(Pointer conv);


    public native int wbxml_conv_xml2wbxml_create(PointerByReference convPtr);

    public native void wbxml_conv_xml2wbxml_set_version(Pointer conv, int version);
    public native void wbxml_conv_xml2wbxml_enable_preserve_whitespaces(Pointer conv);
    public native void wbxml_conv_xml2wbxml_disable_string_table(Pointer conv);
    public native void wbxml_conv_xml2wbxml_disable_public_id(Pointer conv);

    public native int wbxml_conv_xml2wbxml_run(Pointer conv, byte[] xml, long xmlLength, PointerByReference wbxmlPtr, LongByReference wbxmlLength);

    public native void wbxml_conv_xml2wbxml_destroy(Pointer conv);


    public native String wbxml_errors_string(int ret);
}
