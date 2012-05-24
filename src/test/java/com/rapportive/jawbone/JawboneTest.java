package com.rapportive.jawbone;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import static com.rapportive.jawbone.JawboneException.check;

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
        WbxmlToXml conv = new WbxmlToXml();
        conv.setGenType(2); /* WBXML_GEN_XML_CANONICAL */
        conv.setLanguage(2402); /* WBXML_LANG_ACTIVESYNC */
        conv.setCharset(0); /* WBXML_CHARSET_UNKNOWN */
        conv.setIndent(4); /* 4-space indent */
        conv.enablePreserveWhitespaces();

        byte[] xmlBytes = conv.run(wbxml);
        assertTrue("returned no XML", xmlBytes.length > 0);
    }

    @Test
    public void testXmlToWbxml() throws Exception {
        XmlToWbxml conv = new XmlToWbxml();

        conv.enablePreserveWhitespaces();
        conv.disableStringTable();
        conv.disablePublicId();

        byte[] wbxmlBytes = conv.run(xml);
        assertTrue("returned no WBXML", wbxmlBytes.length > 0);
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
