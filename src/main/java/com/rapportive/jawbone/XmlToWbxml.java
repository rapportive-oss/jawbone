package com.rapportive.jawbone;

import static com.rapportive.jawbone.JawboneException.check;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class XmlToWbxml {
    static {
        Native.register("wbxml2");
    }


    private final Pointer conv;


    public XmlToWbxml() {
        PointerByReference convPtr = new PointerByReference();
        check("wbxml_conv_xml2wbxml_create", wbxml_conv_xml2wbxml_create(convPtr));
        conv = convPtr.getValue();
    }

    public void enablePreserveWhitespaces() {
        wbxml_conv_xml2wbxml_enable_preserve_whitespaces(conv);
    }
    public void disableStringTable() {
        wbxml_conv_xml2wbxml_disable_string_table(conv);
    }
    public void disablePublicId() {
        wbxml_conv_xml2wbxml_disable_public_id(conv);
    }

    public byte[] run(byte[] xml) {
        PointerByReference wbxmlPtr = new PointerByReference();
        LongByReference wbxmlLength = new LongByReference();

        check("wbxml_conv_xml2wbxml_run", wbxml_conv_xml2wbxml_run(conv, xml, xml.length, wbxmlPtr, wbxmlLength));

        byte[] wbxmlBytes = wbxmlPtr.getValue().getByteArray(0L, (int) wbxmlLength.getValue());
        Native.free(Pointer.nativeValue(wbxmlPtr.getValue()));
        return wbxmlBytes;
    }


    private native int wbxml_conv_xml2wbxml_create(PointerByReference convPtr);

    private native void wbxml_conv_xml2wbxml_enable_preserve_whitespaces(Pointer conv);
    private native void wbxml_conv_xml2wbxml_disable_string_table(Pointer conv);
    private native void wbxml_conv_xml2wbxml_disable_public_id(Pointer conv);

    private native int wbxml_conv_xml2wbxml_run(Pointer conv, byte[] xml, long xmlLength, PointerByReference wbxmlPtr, LongByReference wbxmlLength);

    private native void wbxml_conv_xml2wbxml_destroy(Pointer conv);
}
