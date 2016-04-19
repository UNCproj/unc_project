/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unc.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Crypt2 {
  static String IV = "AAAAAAAAAAAAAAAA";
  static String encryptionKey = "0123456789abcdef";

  public static String encrypt(String plainText) throws Exception {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
      return new Base64().encodeAsString(cipher.doFinal(plainText.getBytes("UTF-8")));
  }

  public static String decrypt(String cipherText) throws Exception{
      byte[] text = new Base64().decodeBase64(cipherText);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
      return new String(cipher.doFinal(text),"UTF-8");
  }
}