package com.rapportive.jawbone;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class WbxmlToXml {
    static {
        Native.register("wbxml2");
    }


    private final Pointer conv;


    public WbxmlToXml() {
        PointerByReference convPtr = new PointerByReference();
        check("wbxml_conv_wbxml2xml_create", wbxml_conv_wbxml2xml_create(convPtr));
        conv = convPtr.getValue();
    }

    public void setGenType(int genType) {
        wbxml_conv_wbxml2xml_set_gen_type(conv, genType);
    }
    public void setLanguage(int language) {
        wbxml_conv_wbxml2xml_set_language(conv, language);
    }
    public void setCharset(int charset) {
        wbxml_conv_wbxml2xml_set_charset(conv, charset);
    }
    public void setIndent(int indent) {
        wbxml_conv_wbxml2xml_set_indent(conv, indent);
    }
    public void enablePreserveWhitespaces() {
        wbxml_conv_wbxml2xml_enable_preserve_whitespaces(conv);
    }

    public byte[] run(byte[] wbxml) {
        PointerByReference xmlPtr = new PointerByReference();
        LongByReference xmlLength = new LongByReference();

        check("wbxml_conv_wbxml2xml_run", wbxml_conv_wbxml2xml_run(conv, wbxml, wbxml.length, xmlPtr, xmlLength));

        byte[] xmlBytes = xmlPtr.getValue().getByteArray(0L, (int) xmlLength.getValue());
        Native.free(Pointer.nativeValue(xmlPtr.getValue()));
        return xmlBytes;
    }

    @Override
    public void finalize() {
        wbxml_conv_wbxml2xml_destroy(conv);
    }


    private void check(String function, int ret) {
        if (ret != 0) {
            throw new WbxmlToXmlException(function + ": " + wbxml_errors_string(ret));
        }
    }

    @SuppressWarnings("serial")
    private class WbxmlToXmlException extends RuntimeException {
        public WbxmlToXmlException(String message) {
            super(message);
        }
    }


    private native int wbxml_conv_wbxml2xml_create(PointerByReference convPtr);

    private native void wbxml_conv_wbxml2xml_set_gen_type(Pointer conv, int genType);
    private native void wbxml_conv_wbxml2xml_set_language(Pointer conv, int language);
    private native void wbxml_conv_wbxml2xml_set_charset(Pointer conv, int charset);
    private native void wbxml_conv_wbxml2xml_set_indent(Pointer conv, int indent);
    private native void wbxml_conv_wbxml2xml_enable_preserve_whitespaces(Pointer conv);

    private native int wbxml_conv_wbxml2xml_run(Pointer conv, byte[] wbxml, long wbxmlLength, PointerByReference xmlPtr, LongByReference xmlLength);

    private native void wbxml_conv_wbxml2xml_destroy(Pointer conv);

    private native String wbxml_errors_string(int ret);
}
