package KrypteringOpgave;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class Keys {
    private static KeyPair pair;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static void main(String[] args) {
        genKeys();
        gemPrivateKey(privateKey);
        gemPublicKey(publicKey);
    }
    public static void genKeys() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            pair = generator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        System.out.println("Keys created");
    }

    public static void gemPublicKey(PublicKey pk) {
        try {
            FileOutputStream fos = new FileOutputStream("public.key");
            fos.write(pk.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void gemPrivateKey(PrivateKey pk) {
        try {
            FileOutputStream fos = new FileOutputStream("private.key");
            fos.write(pk.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
