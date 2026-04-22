package com.spike.dataengineering.cryptography;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Hexadecimals;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

// Diffie-Hellman(DH) key exchange
public class DHTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void dh() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException {
        // see: https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keypairgenerator-algorithms
        KeyPairGenerator gen = KeyPairGenerator.getInstance("DH", "BC");

        gen.initialize(512, new SecureRandom());

        KeyPair keyPairA = gen.generateKeyPair();
        PrivateKey privateA = keyPairA.getPrivate();
        PublicKey publicA = keyPairA.getPublic();
        System.out.println("PrivateKey A: " + privateA);
        System.out.println("PublicKey A: " + publicA);

        KeyPair keyPairB = gen.generateKeyPair();
        PrivateKey privateB = keyPairB.getPrivate();
        PublicKey publicB = keyPairB.getPublic();
        System.out.println("PrivateKey B: " + privateB);
        System.out.println("PublicKey B: " + publicB);

        byte[] secretA = this.doDH(privateA, publicB);
        byte[] secretB = this.doDH(privateB, publicA);
        System.out.println("Secret A: " + Hexadecimals.toHexString(secretA));
        System.out.println("Secret B: " + Hexadecimals.toHexString(secretB));
        Assertions.assertThat(secretA).asHexString()
                .isEqualTo(Hexadecimals.toHexString(secretB));
    }

    @Test
    public void ecdh() throws NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {
        // see: https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keypairgenerator-algorithms
        KeyPairGenerator gen = KeyPairGenerator.getInstance("ECDH", "BC");

        // see: https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#parameterspec-names
        gen.initialize(new ECGenParameterSpec("secp192k1"), new SecureRandom());

        KeyPair keyPairA = gen.generateKeyPair();
        PrivateKey privateA = keyPairA.getPrivate();
        PublicKey publicA = keyPairA.getPublic();
        System.out.println("PrivateKey A: " + privateA);
        System.out.println("PublicKey A: " + publicA);

        KeyPair keyPairB = gen.generateKeyPair();
        PrivateKey privateB = keyPairB.getPrivate();
        PublicKey publicB = keyPairB.getPublic();
        System.out.println("PrivateKey B: " + privateB);
        System.out.println("PublicKey B: " + publicB);

        byte[] secretA = this.doECDH(privateA, publicB);
        byte[] secretB = this.doECDH(privateB, publicA);
        System.out.println("Secret A: " + Hexadecimals.toHexString(secretA));
        System.out.println("Secret B: " + Hexadecimals.toHexString(secretB));
        Assertions.assertThat(secretA).asHexString()
                .isEqualTo(Hexadecimals.toHexString(secretB));
    }

    private byte[] doDH(PrivateKey privateKey, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        // see https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keyagreement-algorithms
        KeyAgreement ka = KeyAgreement.getInstance("DiffieHellman", "BC");
        ka.init(privateKey);
        ka.doPhase(publicKey, true);
        byte[] secret = ka.generateSecret();
        return secret;
    }

    private byte[] doECDH(PrivateKey privateKey, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        // see https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keyagreement-algorithms
        KeyAgreement ka = KeyAgreement.getInstance("ECDH", "BC");
        ka.init(privateKey);
        ka.doPhase(publicKey, true);
        byte[] secret = ka.generateSecret();
        return secret;
    }
}
