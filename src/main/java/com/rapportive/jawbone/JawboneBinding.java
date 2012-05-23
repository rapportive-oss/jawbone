package com.rapportive.jawbone;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface JawboneBinding extends Library {
    JawboneBinding INSTANCE = (JawboneBinding) Native.loadLibrary("wbxml2", JawboneBinding.class);

    int wbxml_conv_wbxml2xml_create(PointerByReference jawbonePtr);

    void wbxml_conv_wbxml2xml_set_gen_type(Pointer jawbone, int genType);
    void wbxml_conv_wbxml2xml_set_language(Pointer jawbone, int language);
    void wbxml_conv_wbxml2xml_set_charset(Pointer jawbone, int charset);
    void wbxml_conv_wbxml2xml_set_indent(Pointer jawbone, int indent);
    void wbxml_conv_wbxml2xml_enable_preserve_whitespaces(Pointer jawbone);

    int wbxml_conv_wbxml2xml_run(Pointer jawbone, byte[] wbxml, long wbxmlLength, PointerByReference xmlPtr, LongByReference xmlLength);

    void wbxml_conv_wbxml2xml_destroy(Pointer jawbone);
}
