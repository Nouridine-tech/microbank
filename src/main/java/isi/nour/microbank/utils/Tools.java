package isi.nour.microbank.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Tools {

    // Génère un numéro de compte unique — format : MB-20260822-4872
    public static String generateAccountNumber() {
        Random random = new Random();
        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String suffix = String.format("%04d", random.nextInt(10000));
        return "MB-" + date + "-" + suffix;
    }

    // Génère une référence unique pour chaque opération — format : OP-20260822-3741
    public static String generateOperationReference() {
        Random random = new Random();
        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String suffix = String.format("%04d", random.nextInt(10000));
        return "OP-" + date + "-" + suffix;
    }

    // Hache le mot de passe en SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 non disponible", e);
        }
    }
}