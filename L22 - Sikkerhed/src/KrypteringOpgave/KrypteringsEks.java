package KrypteringOpgave;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KrypteringsEks {
    private static byte[] encryptedMessageBytes;
    private static PublicKey publicKey;

    public static void main(String[] args) {
        kryptere("Mega hemmelig besked der er krypteret");
        dekryptere("messageMedKryp");
    }

    public static PublicKey hentPublicKey() {
        File publicKeyFile = new File("public.key");
        PublicKey pubKey;
        byte[] publicKeyBytes;

        try {
            publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("hentet public");
        return publicKey;
    }

    public static PrivateKey hentPrivateKey() {
        File privateKeyFile = new File("private.key");
        PrivateKey privateKey;
        byte[] privateKeyBytes;
        try {
            privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        System.out.println("hentet private");
        return privateKey;
    }

    public static void kryptere(String message) {
        Cipher encryptCipher;
        String encodedMessage;
        try {
            encryptCipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            encryptCipher.init(Cipher.ENCRYPT_MODE, hentPublicKey());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);

        try {
            encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream("messageMedKryp");
            fos.write(encryptedMessageBytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Dette er den kodet besked: " + encodedMessage);
    }

    public static void dekryptere(String filNavn) {

        Cipher decryptCipher = null;
        byte[] messageBytes;
        try {
            decryptCipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            decryptCipher.init(Cipher.DECRYPT_MODE, hentPrivateKey());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        File messageFile = new File(filNavn);

        try {
            messageBytes = Files.readAllBytes(messageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] decryptedMessageBytes = new byte[0];
        try {
            decryptedMessageBytes = decryptCipher.doFinal(messageBytes);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        System.out.println("Dette er beskeden: " + decryptedMessage);
    }
}


