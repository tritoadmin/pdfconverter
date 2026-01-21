package kr.co.trito.pdf.util;

//import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static byte[] decode(String encodeStr) {
    	return Base64.decodeBase64(encodeStr);
    }

//    public static String encode(byte[] bytes) {
//    	return DatatypeConverter.printBase64Binary(bytes);
//    }
//
//    public static byte[] decode(String encodeStr) {
//    	return DatatypeConverter.parseHexBinary(encodeStr);
//    }


}
