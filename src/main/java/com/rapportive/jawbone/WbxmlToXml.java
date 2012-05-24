package com.rapportive.jawbone;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class WbxmlToXml {
    private final JawboneBinding jawbone;
    private final Pointer conv;


    WbxmlToXml(JawboneBinding jawbone) {
        this.jawbone = jawbone;

        PointerByReference convPtr = new PointerByReference();
        jawbone.check(jawbone.wbxml_conv_wbxml2xml_create(convPtr));
        conv = convPtr.getValue();
    }

    public void setGenType(int genType) {
        jawbone.wbxml_conv_wbxml2xml_set_gen_type(conv, genType);
    }
    public void setLanguage(int language) {
        jawbone.wbxml_conv_wbxml2xml_set_language(conv, language);
    }
    public void setCharset(int charset) {
        jawbone.wbxml_conv_wbxml2xml_set_charset(conv, charset);
    }
    public void setIndent(int indent) {
        jawbone.wbxml_conv_wbxml2xml_set_indent(conv, indent);
    }
    public void enablePreserveWhitespaces() {
        jawbone.wbxml_conv_wbxml2xml_enable_preserve_whitespaces(conv);
    }

    public byte[] run(byte[] wbxml) {
        PointerByReference xmlPtr = new PointerByReference();
        LongByReference xmlLength = new LongByReference();

        jawbone.check(jawbone.wbxml_conv_wbxml2xml_run(conv, wbxml, wbxml.length, xmlPtr, xmlLength));

        byte[] xmlBytes = xmlPtr.getValue().getByteArray(0L, (int) xmlLength.getValue());
        Native.free(Pointer.nativeValue(xmlPtr.getValue()));
        return xmlBytes;
    }

    @Override
    public void finalize() {
        jawbone.wbxml_conv_wbxml2xml_destroy(conv);
    }
}
