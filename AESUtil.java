package indi.newbieking;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    /**
     * 
     * @param key AES secret key: 128/192/256 bits
     * @param message message to be encrypted
     * @return encrypted message
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    public static byte[] encrypt(byte[] key, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        byte[] iv = SecureRandom.getInstanceStrong().generateSeed(16);
        IvParameterSpec ivps = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps);
        byte[] data = cipher.doFinal(message);
        return join(iv, data);
    }

    /**
     * 
     * @param key AES secret key: 128/192/256 bits
     * @param message message to be decrypted
     * @return decrypted message
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    public static byte[] decrypt(byte[] key, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Pair pair = extract(message);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivps = new IvParameterSpec(pair.getIv());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivps);
        return cipher.doFinal(pair.getMsg());
    }

    /**
     * join two bytes arrays into a new array
     * @param a first bytes array to be joind
     * @param b second bytes array to be joind
     * @return the joind bytes array
     */
    public static byte[] join(byte[] a, byte[] b) {
        byte[] bytes = new byte[a.length + b.length];
        System.arraycopy(a, 0, bytes, 0, a.length);
        System.arraycopy(b, 0, bytes, 16, b.length);
        return bytes;
    }

    /**
     * a data class named pair with iv and msg fields
     */
    private static class Pair {
        private final byte[] iv;
        private final byte[] msg;

        public Pair(byte[] iv, byte[] msg) {
            this.iv = iv;
            this.msg = msg;
        }

        public byte[] getIv() {
            return iv;
        }

        public byte[] getMsg() {
            return msg;
        }
    }

    /**
     * extract the given bytes array to 16-bits iv bytes array and the rest message bytes array
     * @param bytes
     * @return pair with iv and message bytes array
     */
    public static Pair extract(byte[] bytes) {
        byte[] iv = new byte[16];
        byte[] msg = new byte[bytes.length - 16];
        System.arraycopy(bytes, 0, iv, 0, 16);
        System.arraycopy(bytes, 16, msg, 0, bytes.length - 16);
        return new Pair(iv, msg);
    }

}
