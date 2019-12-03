package fr.gnark.sound.port.websocket;

import fr.gnark.sound.applications.ScaleAGogo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ScalePlayerHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions;
    private final ScaleAGogo application;

    public ScalePlayerHandler(final ScaleAGogo application) {
        this.sessions = new CopyOnWriteArrayList<>();
        this.application = application;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        final byte[] data= new byte[10000];
        data[0] = (byte)1;
        for(WebSocketSession webSocketSession : sessions) {

            webSocketSession.sendMessage(new BinaryMessage(data));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }
}
