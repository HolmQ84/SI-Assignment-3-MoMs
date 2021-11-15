package si.assignment3.service;

import java.util.Random;

public class IdGenerator {
    private String generatedId;

    public void generateId(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));
        generatedId = sb.toString();
    }

    public String getId() {
        generateId(20);
        return generatedId;
    }
}
