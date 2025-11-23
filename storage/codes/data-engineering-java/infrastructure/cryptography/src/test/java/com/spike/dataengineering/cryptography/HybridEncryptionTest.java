package com.spike.dataengineering.cryptography;

import com.google.crypto.tink.HybridDecrypt;
import com.google.crypto.tink.HybridEncrypt;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Hexadecimals;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;

public class HybridEncryptionTest {

    static {
        try {
            HybridConfig.register();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ecies() throws GeneralSecurityException {
        KeysetHandle privateHandle = KeysetHandle.generateNew(
                HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
        KeysetHandle publicHandle = privateHandle.getPublicKeysetHandle();

        final String plaintext = "hello";
        final String associatedData = "from Alice";

        HybridEncrypt hybridEncrypt = publicHandle.getPrimitive(HybridEncrypt.class);
        byte[] ciphertext = hybridEncrypt.encrypt(plaintext.getBytes(), associatedData.getBytes());
        System.out.println(Hexadecimals.toHexString(ciphertext));

        HybridDecrypt hybridDecrypt = privateHandle.getPrimitive(HybridDecrypt.class);
        byte[] plaintextD = hybridDecrypt.decrypt(ciphertext, associatedData.getBytes());

        Assertions.assertThat(new String(plaintextD)).isEqualTo(plaintext);
    }
}
