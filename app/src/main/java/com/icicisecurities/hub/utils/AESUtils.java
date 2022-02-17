package com.icicisecurities.hub.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static String TAG = AESUtils.class.getSimpleName();

        private static final byte[] keyValue = new byte[]{'i', 'd', 'i', 'r', 'e', 'c', 't', 'd', 'o', 't', 'c', 'o', 'm','c','o','m'};

        public static String encrypt( String cleartext) throws Exception {
            byte[] rawKey = getRawKey(keyValue);
            byte[] result = encrypt(rawKey, cleartext.getBytes());
            return toBase64(result);
        }

        public static String decrypt( String encrypted) throws Exception {
            byte[] rawKey = getRawKey(keyValue);
            byte[] enc = fromBase64(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        }

        private static byte[] getRawKey(byte[] seed) throws Exception {
            SecretKey skey = new SecretKeySpec(seed, "AES");
            byte[] raw = skey.getEncoded();
            return raw;
        }

        private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        }

        private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        }

        public static String toBase64(byte[] buf)
        {
            return Base64.encodeToString(buf , Base64.DEFAULT);
//        return Base64.encodeBytes(buf);
        }

        public static byte[] fromBase64(String str) throws Exception
        {
            return Base64.decode(str, Base64.DEFAULT);
        }
}
