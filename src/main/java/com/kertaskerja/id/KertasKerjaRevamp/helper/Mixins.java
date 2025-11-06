package com.kertaskerja.id.KertasKerjaRevamp.helper;

public class Mixins {

    public static String generateRandomId(String prefix, String year) {
        int number = new java.security.SecureRandom().nextInt(10000);
        return String.format("%s-%s-%04d", prefix, year, number);
    }
}