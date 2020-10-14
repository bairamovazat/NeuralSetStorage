package ru.azat.utils;

import java.util.Base64;
import java.util.Random;

public class GenerateImageDataUtils {

    private GenerateImageDataUtils() {

    }

    public static byte[] generateImageData() {
        byte[] data = new byte[250 * 250 * 3];

        Random random = new Random();

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) 0;
        }

        return data;
    }

    public static String generateBase64ImageData() {
        return Base64.getEncoder().encodeToString(generateImageData());
    }

    public static void main(String[] args) {
        System.out.println(generateBase64ImageData());
    }

}
