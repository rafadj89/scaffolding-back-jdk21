package com.invima.scaffolding.application.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";


    public static SecretKey getPredefinedAESKey(String PREDEFINED_AES_KEY) {
        byte[] decodedKey = Base64.getDecoder().decode(PREDEFINED_AES_KEY);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // Encriptar el DTO con la clave predefinida
    public static String encrypt(String json, String aesKey) throws Exception {

        // Encriptar el JSON usando AES
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getPredefinedAESKey(aesKey));
        byte[] encryptedBytes = cipher.doFinal(json.getBytes());

        // Codificar el resultado en Base64
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
