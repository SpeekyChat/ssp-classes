package org.restudios.gateway.client.ssp;

public interface TokenStorer {
    /**
     * Get saved auth token
     * @return Token. Null if token not saved
     */
    public String getSavedToken();
    public void storeSavedToken(String token);
}
