package com.rapportive.jawbone;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
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
        assertNotNull(jawbone);
    }
}
