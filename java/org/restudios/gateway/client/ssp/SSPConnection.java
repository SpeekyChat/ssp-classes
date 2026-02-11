package org.restudios.gateway.client.ssp;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public abstract class SSPConnection {

    private final StateAndListener<ConnectionState> connectionState = new StateAndListener<>(ConnectionState.DISCONNECTED);
    private final StateAndListener<SspClasses.GeneralClasses.Auth.AuthState> authState = new StateAndListener<>(null);
    private SSP ssp;
    private final ObjectSerializer serializer;
    private final Map<Long, Set<Function<SspClasses.GeneralClasses.SlClass, Boolean>>> replyListeners;
    private final Set<Consumer<SspClasses.GeneralClasses.SlClass>> incomingPacketListeners;
    private final Set<Consumer<SspClasses.GeneralClasses.SlClass>> outgoingPacketListeners;
    private final Set<Consumer<SspClasses.GeneralClasses.Auth.AuthorizationError>> authErrorListeners;
    private final AtomicLong latestPacketId = new AtomicLong(0L);
    private final AtomicBoolean tokenAttempted = new AtomicBoolean(false);
    private final TokenStorer tokenStorer;

    public SSPConnection(TokenStorer tokenStorer) {
        this.tokenStorer = tokenStorer;
        this.serializer = new ObjectSerializer();
        this.replyListeners = new ConcurrentHashMap<>();
        this.incomingPacketListeners = ConcurrentHashMap.newKeySet();
        this.outgoingPacketListeners = ConcurrentHashMap.newKeySet();
        this.authErrorListeners = ConcurrentHashMap.newKeySet();
        this.latestPacketId.set(0L);
        this.tokenAttempted.set(false);
        connectionState.setState(ConnectionState.DISCONNECTED);
        authState.setState(null);
    }

    protected abstract void disconnect();

    protected abstract void connect();

    protected abstract void sendRawPacket(byte[] message);

    protected final void onConnectionOpen() {
        connectionState.setState(ConnectionState.KEY_EXCHANGE);
    }

    /**
     * @param e null if not exception
     */
    protected final void onConnectionClose(Exception e) {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
        connect();
    }

    protected final void handleRawPacket(byte[] message) {
        if (connectionState.getState() == ConnectionState.KEY_EXCHANGE) {
            handleKeyExchange(message);
        } else if (connectionState.getState() == ConnectionState.CONNECTED) {
            handlePacket(message);
        } else {
            System.err.println("Received packet while not connected! is not connected");
        }
    }

    private void handlePacket(byte[] raw) {
        if (ssp == null) {
            System.err.println("Received packet while not connected! SSP null");
            return;
        }
        try {
            SSPPacket packet = SSPPacket.read(ssp, raw);
            Object object = serializer.deserialize(packet.getData());
            processPacket((SspClasses.GeneralClasses.SlClass) object, packet.getReply());
        } catch (Exception e) {
            System.err.println("Failed to read packet");
            e.printStackTrace(System.err);
        }
    }

    private void processPacket(SspClasses.GeneralClasses.SlClass slClass, Long reply) {
        for (Consumer<SspClasses.GeneralClasses.SlClass> packetListener : this.incomingPacketListeners) {
            try {
                packetListener.accept(slClass);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        if (slClass instanceof SspClasses.GeneralClasses.Auth.AuthState authState) {
            if (authState instanceof SspClasses.GeneralClasses.Auth.WaitingForAuthType) {
                String client = "Speeky Mobile Client 1.0"; // todo app client name
                String token = tokenStorer.getSavedToken();
                if (token != null && !tokenAttempted.get()) {
                    sendObjectSafe(new SspClasses.GeneralClasses.Auth.AuthTypeInput(SspClasses.GeneralClasses.Auth.AuthType.TOKEN, client));
                    tokenAttempted.set(true);
                } else if (token == null) {
                    sendObjectSafe(new SspClasses.GeneralClasses.Auth.AuthTypeInput(SspClasses.GeneralClasses.Auth.AuthType.EMAIL, client));
                }
            } else if (authState instanceof SspClasses.GeneralClasses.Auth.WaitingForToken) {
                if (tokenAttempted.get()) {
                    sendObjectSafe(new SspClasses.GeneralClasses.Auth.TokenInput(tokenStorer.getSavedToken()));
                }
            } else if (authState instanceof SspClasses.GeneralClasses.Auth.SuccessfullyAuthorization func) {
                if (func.token != null) {
                    this.tokenStorer.storeSavedToken(func.token);
                }
            } else if (authState instanceof SspClasses.GeneralClasses.Auth.AuthorizationError error) {
                this.authErrorListeners.forEach(listener -> {
                    try {
                        listener.accept(error);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                });
            }
            this.authState.setState(authState);
        }

        if (reply != null && this.replyListeners.containsKey(reply)) {
            List<Function<SspClasses.GeneralClasses.SlClass, Boolean>> remove = new ArrayList<>();
            for (Function<SspClasses.GeneralClasses.SlClass, Boolean> slClassBooleanFunction : this.replyListeners.get(reply)) {
                try {
                    if (slClassBooleanFunction.apply(slClass)) {
                        remove.add(slClassBooleanFunction);
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    remove.add(slClassBooleanFunction);
                }
            }
            remove.forEach(this.replyListeners.get(reply)::remove);
            if (replyListeners.get(reply).isEmpty()) replyListeners.remove(reply);
        }

    }

    public final Runnable addPacketListener(Consumer<SspClasses.GeneralClasses.SlClass> packetListener) {
        incomingPacketListeners.add(packetListener);
        return () -> incomingPacketListeners.remove(packetListener);
    }
    public final Runnable addOutgoingPacketListener(Consumer<SspClasses.GeneralClasses.SlClass> packetListener) {
        outgoingPacketListeners.add(packetListener);
        return () -> outgoingPacketListeners.remove(packetListener);
    }
    public final Runnable addAuthErrorListener(Consumer<SspClasses.GeneralClasses.SlClass> packetListener) {
        incomingPacketListeners.add(packetListener);
        return () -> incomingPacketListeners.remove(packetListener);
    }

    public final void sendObject(SspClasses.GeneralClasses.SlClass obj, Long reply) throws Exception {
        long packetId = this.pickPacketId();

        for (Consumer<SspClasses.GeneralClasses.SlClass> packetListener : this.outgoingPacketListeners) {
            try {
                packetListener.accept(obj);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        byte[] serialized = serializer.serialize(obj);
        SSPPacket packet = new SSPPacket(packetId, reply, serialized);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        packet.write(ssp, byteArrayOutputStream);
        sendRawPacket(byteArrayOutputStream.toByteArray());
    }

    /**
     * @param callback Function receives a reply object and returns true if parsed successfully & removes listener
     */
    public final void sendObject(SspClasses.GeneralClasses.SlClass obj, Long reply, Function<SspClasses.GeneralClasses.SlClass, Boolean> callback) throws Exception {
        long packetId = this.pickPacketId();

        for (Consumer<SspClasses.GeneralClasses.SlClass> packetListener : this.outgoingPacketListeners) {
            try {
                packetListener.accept(obj);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        byte[] serialized = serializer.serialize(obj);
        SSPPacket packet = new SSPPacket(packetId, reply, serialized);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        packet.write(ssp, byteArrayOutputStream);
        sendRawPacket(byteArrayOutputStream.toByteArray());

        replyListeners.computeIfAbsent(packetId, aLong -> HashSet.newHashSet(0)).add(callback);
    }

    public final void sendObject(SspClasses.GeneralClasses.SlClass obj, Long reply, Consumer<SspClasses.GeneralClasses.SlClass> callback) throws Exception {
        sendObject(obj, reply, (obj1) -> {
            callback.accept(obj1);
            return true;
        });
    }

    public final void sendObjectSafe(SspClasses.GeneralClasses.SlClass obj) {
        try {
            this.sendObject(obj);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public final void sendObject(SspClasses.GeneralClasses.SlClass obj) throws Exception {
        sendObject(obj, (Long) null);
    }

    public final void sendObject(SspClasses.GeneralClasses.SlClass obj, Consumer<SspClasses.GeneralClasses.SlClass> callback) throws Exception {
        sendObject(obj, null, callback);
    }

    private void handleKeyExchange(byte[] raw) {
        String[] parts = new String(raw).split(":");
        String gStr = parts[0];
        String pStr = parts[1];
        String serverPublicStr = parts[2];

        BigInteger g = new BigInteger(gStr);
        BigInteger p = new BigInteger(pStr);
        BigInteger serverPublic = new BigInteger(serverPublicStr);

        BigInteger clientSecret = generatePrivateKey();
        BigInteger clientPublic = g.modPow(clientSecret, p);
        sendRawPacket(clientPublic.toString().getBytes());

        BigInteger sharedSecret = serverPublic.modPow(clientSecret, p);
        ssp = new SSP(sharedSecret);

        connectionState.setState(ConnectionState.CONNECTED);
        System.out.println("Connected!");
    }

    private BigInteger generatePrivateKey() {
        return BigInteger.probablePrime(2048, new java.util.Random());
    }

    private long pickPacketId() {
        return latestPacketId.getAndIncrement();
    }
}
