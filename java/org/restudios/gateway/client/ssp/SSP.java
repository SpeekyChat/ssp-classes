package org.restudios.gateway.client.ssp;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.restudios.gateway.ssp.SSPEncryptor;
import org.restudios.gateway.ssp.SSPManager;
import org.restudios.gateway.ssp.SSPSharedPair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Speeky * swimery
 * org.restudios.gateway.ssp
 *
 * @author ReStudios 26 07 2025
 */
public class SSP {
    private final BigInteger sharedSecretKey;

    public SSP(BigInteger sharedSecretKey) {
        this.sharedSecretKey = sharedSecretKey;
    }

    public String encrypt(String message) throws Exception {
        if (this.sharedSecretKey == null) {
            throw new IllegalStateException("Uninitialized");
        }
        return SSPEncryptor.encrypt(message, this.sharedSecretKey);
    }

    public byte[] encrypt(byte[] message) throws Exception {
        if (this.sharedSecretKey == null) {
            throw new IllegalStateException("Uninitialized");
        }
        return SSPEncryptor.encrypt(message, this.sharedSecretKey);
    }

    public String decrypt(String encrypted) throws Exception {
        if (this.sharedSecretKey == null) {
            throw new IllegalStateException("Uninitialized");
        }
        return SSPEncryptor.decrypt(encrypted, this.sharedSecretKey);
    }

    public byte[] decrypt(byte[] encrypted) throws Exception {
        if (this.sharedSecretKey == null) {
            throw new IllegalStateException("Uninitialized");
        }
        return SSPEncryptor.decrypt(encrypted, this.sharedSecretKey);
    }
}
