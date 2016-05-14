/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unc.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Crypt2 {
  private static String IV = "AAAAAAAAAAAAAAAA";
  private static String encryptionKey = "0123456789abcdef";

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
  
  public static String sha256(String base) {
      try {
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          byte[] hash = digest.digest(base.getBytes("UTF-8"));
          StringBuffer hexString = new StringBuffer();
          for (int i = 0; i < hash.length; i++) {
              String hex = Integer.toHexString(0xff & hash[i]);
              if(hex.length() == 1) hexString.append('0');
              hexString.append(hex);
          }
          return hexString.toString();
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
          Logger.getLogger(Crypt2.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
    }
}