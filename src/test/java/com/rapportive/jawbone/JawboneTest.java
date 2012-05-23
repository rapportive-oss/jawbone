package com.rapportive.jawbone;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.sun.jna.Pointer;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class JawboneTest {

    private static JawboneBinding binding;
    private PointerByReference jawbonePtr = new PointerByReference();
    private Pointer jawbone;

    private static byte[] wbxml;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        wbxml = readFile(testResource("activesync-001-settings_device_information.wbxml"));

        binding = JawboneBinding.INSTANCE;
    }

    @Before
    public void setUp() throws Exception {
        int ret = binding.wbxml_conv_wbxml2xml_create(jawbonePtr);
        assertEquals("wbxml_conv_wbxml2xml_create failed", 0, ret);
        jawbone = jawbonePtr.getValue();
        // TODO worry about cleanup
    }

    @Test
    public void test() throws Exception {
        binding.wbxml_conv_wbxml2xml_set_gen_type(jawbone, 2); /* WBXML_GEN_XML_CANONICAL */
        binding.wbxml_conv_wbxml2xml_set_language(jawbone, 2402); /* WBXML_LANG_ACTIVESYNC */
        binding.wbxml_conv_wbxml2xml_set_charset(jawbone, 0); /* WBXML_CHARSET_UNKNOWN */
        binding.wbxml_conv_wbxml2xml_set_indent(jawbone, 4); /* 4-space indent */
        binding.wbxml_conv_wbxml2xml_enable_preserve_whitespaces(jawbone);

        PointerByReference xmlPtr = new PointerByReference();
        // TODO worry about cleanup
        LongByReference xmlLength = new LongByReference();
        int ret = binding.wbxml_conv_wbxml2xml_run(jawbone, wbxml, wbxml.length, xmlPtr, xmlLength);

        assertEquals("wbxml_conv_wbxml2xml_run failed", 0, ret);
        assertTrue("returned no XML", xmlLength.getValue() > 0);

        byte[] xmlBytes = xmlPtr.getValue().getByteArray(0L, (int) xmlLength.getValue());
        assertEquals("returned b0rked bytes", xmlLength.getValue(), xmlBytes.length);
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
