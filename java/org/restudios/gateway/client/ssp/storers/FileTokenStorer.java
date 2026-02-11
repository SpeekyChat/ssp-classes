package org.restudios.gateway.client.ssp.storers;

import org.restudios.gateway.client.ssp.TokenStorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileTokenStorer implements TokenStorer {
    private final File token;

    public FileTokenStorer(String filename) {
        this.token = new File(filename);
        if (!token.exists()) {
            try {
                Files.createFile(token.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getSavedToken() {
        try {
            String tkn = Files.readString(token.toPath());
            if (tkn == null || tkn.isBlank()) return null;
            return tkn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void storeSavedToken(String token) {
        try {
            Files.writeString(this.token.toPath(), token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
