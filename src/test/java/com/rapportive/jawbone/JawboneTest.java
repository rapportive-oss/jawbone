package com.rapportive.jawbone;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class JawboneTest {

    private static JawboneBinding binding;

    private static byte[] wbxml;
    private static byte[] xml;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        wbxml = readFile(testResource("activesync-001-settings_device_information.wbxml"));
        xml = readFile(testResource("activesync-001-settings_device_information.xml"));

        binding = new JawboneBinding();
    }

    @Test
    public void testWbxmlToXml() throws Exception {
        WbxmlToXml conv = binding.newWbxmlToXml();
        conv.setGenType(2); /* WBXML_GEN_XML_CANONICAL */
        conv.setLanguage(2402); /* WBXML_LANG_ACTIVESYNC */
        conv.setCharset(0); /* WBXML_CHARSET_UNKNOWN */
        conv.setIndent(4); /* 4-space indent */
        conv.enablePreserveWhitespaces();

        PointerByReference xmlPtr = new PointerByReference();
        LongByReference xmlLength = new LongByReference();
        conv.run(wbxml, wbxml.length, xmlPtr, xmlLength);

        assertTrue("returned no XML", xmlLength.getValue() > 0);

        byte[] xmlBytes = xmlPtr.getValue().getByteArray(0L, (int) xmlLength.getValue());
        Native.free(Pointer.nativeValue(xmlPtr.getValue()));

        assertEquals("returned b0rked bytes", xmlLength.getValue(), xmlBytes.length);
    }

    @Test
    public void testXmlToWbxml() throws Exception {
        PointerByReference convPtr = new PointerByReference();
        Pointer conv = null;

        int ret = binding.wbxml_conv_xml2wbxml_create(convPtr);
        assertEquals("wbxml_conv_xml2wbxml_create failed", 0, ret);
        conv = convPtr.getValue();

        try {
            binding.wbxml_conv_xml2wbxml_enable_preserve_whitespaces(conv);
            binding.wbxml_conv_xml2wbxml_disable_string_table(conv);
            binding.wbxml_conv_xml2wbxml_disable_public_id(conv);

            PointerByReference wbxmlPtr = new PointerByReference();
            LongByReference wbxmlLength = new LongByReference();
            ret = binding.wbxml_conv_xml2wbxml_run(conv, xml, xml.length, wbxmlPtr, wbxmlLength);

            assertSuccess("wbxml_conv_xml2wbxml_run", ret);
            assertTrue("returned no WBXML", wbxmlLength.getValue() > 0);

            byte[] wbxmlBytes = wbxmlPtr.getValue().getByteArray(0L, (int) wbxmlLength.getValue());
            Native.free(Pointer.nativeValue(wbxmlPtr.getValue()));

            assertEquals("returned b0rked bytes", wbxmlLength.getValue(), wbxmlBytes.length);
        } finally {
            if (conv != null) {
                binding.wbxml_conv_xml2wbxml_destroy(conv);
            }
        }
    }


    private void assertSuccess(String function, int ret) {
        if (ret != 0) {
            fail(function + ": " + binding.wbxml_errors_string(ret));
        }
    }

    private static String testResource(String path) {
        String testResourcesDir = System.getProperty("test.resources");
        if (testResourcesDir == null) {
            throw new IllegalStateException("Must define property test.resources");
        }
        return testResourcesDir + "/" + path;
    }

    private static byte[] readFile(String path) throws IOException {
        InputStream fileIn = null;
        try {
            fileIn = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

            byte[] buf = new byte[4096];
            int read;
            while ((read = fileIn.read(buf)) >= 0) {
                bytesOut.write(buf, 0, read);
            }

            return bytesOut.toByteArray();
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
    }
}
