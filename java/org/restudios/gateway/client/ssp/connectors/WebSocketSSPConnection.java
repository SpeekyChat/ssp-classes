package org.restudios.gateway.client.ssp.connectors;

import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.restudios.gateway.client.ssp.SSPConnection;
import org.restudios.gateway.client.ssp.TokenStorer;

import java.util.concurrent.TimeUnit;

public class WebSocketSSPConnection extends SSPConnection {

    private final OkHttpClient client;
    private WebSocket webSocket;


    public WebSocketSSPConnection(TokenStorer tokenStorer) {
        super(tokenStorer);
        client = new OkHttpClient.Builder()
                .pingInterval(30, TimeUnit.SECONDS) // держим соединение живым
                .build();
    }

    @Override
    public void connect() {
        Request request = new Request.Builder()
                .url("wss://v0vj84fjwnd.speeky.chat/ssp/websocket")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                onConnectionOpen();
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                handleRawPacket(text.getBytes()); // если сервер присылает текст
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                handleRawPacket(bytes.toByteArray()); // если сервер присылает байты
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                onConnectionClose(null); // закрыто нормально
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                onConnectionClose(t instanceof Exception ? (Exception) t : new Exception(t));
            }
        });
    }

    @Override
    protected void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Disconnect");
            webSocket = null;
        }
        client.dispatcher().executorService().shutdown();
    }

    @Override
    protected void sendRawPacket(byte[] message) {
        if (webSocket != null) {
            webSocket.send(ByteString.of(message));
        }
    }

}
