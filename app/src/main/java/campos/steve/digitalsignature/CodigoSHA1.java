package campos.steve.digitalsignature;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Usuario on 23/06/2015.
 */
public class CodigoSHA1 {
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //LA CODIFIACIÓN DEL XML, DEPENDERÁ DE LA CODIFIACIÓN DEL XML. [iso-8859-1 O utf-8]
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md.update(text.getBytes("UTF-8"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String SHA1(byte[] data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //LA CODIFIACIÓN DEL XML, DEPENDERÁ DE LA CODIFIACIÓN DEL XML. [iso-8859-1 O utf-8]
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md.update(data, 0, data.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

}
