package com.goku.im.connector.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by jik on 15/10/30.
 */
public class EncryptUtil {

    public static final byte[] desKey = new byte[]{(byte)64, (byte)-77, (byte)35, (byte)-45, (byte)16, (byte)-22, (byte)121, (byte)-15};

    public EncryptUtil() {
    }

    public static final String encryptString(String value, byte[] desKey) {
        String tmp = value;

        try {
            Cipher e = Cipher.getInstance("DES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(value.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final String encryptString(String userName, String password) {
        String tmp = userName + '\u0002' + password;

        try {
            Cipher e = Cipher.getInstance("DES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(tmp.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final String encryptString(String value) {
        String tmp = value;

        try {
            Cipher e = Cipher.getInstance("DES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(value.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return tmp;
    }

    public static final String decryptString(String source) {
        String tmp = source;

        try {
            byte[] e = decodeHex(tmp);
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher c1 = Cipher.getInstance("DES");
            c1.init(2, key);
            byte[] cipherByte = c1.doFinal(e);
            tmp = new String(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final String decryptString(String source, byte[] desKey) {
        String tmp = source;

        try {
            byte[] e = decodeHex(tmp);
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher c1 = Cipher.getInstance("DES");
            c1.init(2, key);
            byte[] cipherByte = c1.doFinal(e);
            tmp = new String(cipherByte);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return tmp;
    }

    public static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if((bytes[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buf.toString();
    }

    public static final byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;

        for(int i = 0; i < chars.length; i += 2) {
            byte newByte = 0;
            int var6 = newByte | hexCharToByte(chars[i]);
            var6 <<= 4;
            var6 |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte)var6;
            ++byteCount;
        }

        return bytes;
    }

    private static final byte hexCharToByte(char ch) {
        switch(ch) {
            case '0':
                return (byte)0;
            case '1':
                return (byte)1;
            case '2':
                return (byte)2;
            case '3':
                return (byte)3;
            case '4':
                return (byte)4;
            case '5':
                return (byte)5;
            case '6':
                return (byte)6;
            case '7':
                return (byte)7;
            case '8':
                return (byte)8;
            case '9':
                return (byte)9;
            case 'a':
                return (byte)10;
            case 'b':
                return (byte)11;
            case 'c':
                return (byte)12;
            case 'd':
                return (byte)13;
            case 'e':
                return (byte)14;
            case 'f':
                return (byte)15;
            default:
                return (byte)0;
        }
    }

    public static boolean md5PasswordCheck(String dbPassword, String password) {
        if(password == null) {
            return false;
        } else {
            MD5 m = new MD5();
            return m.getMD5ofStr(password).equalsIgnoreCase(dbPassword);
        }
    }

    public static String  decrytUserId(String tokenValue) {
        String[] tokenDecrytes = tokenValue.split("[a-zA-Z]");
        if(tokenDecrytes.length==2){
            return tokenDecrytes[0];
        }
        return null;
    }
}
