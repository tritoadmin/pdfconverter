package kr.co.trito.pdf.util;

import javax.xml.bind.DatatypeConverter;

public class Base64Util {

    public static String encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }
}
