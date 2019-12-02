package fr.gnark.sound.port.websocket;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class PlayerEndpointTest {
    WebSocketClient webSocketClient = new StandardWebSocketClient();
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ScalePlayerHandler scalePlayerHandler;


    @Test
    public void shouldReceiveAMessageFromTheServer() throws Exception {
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        //simulate front app
        webSocketHttpHeaders.setOrigin("http://localhost:8081");
        WebSocketSession webSocketSession = webSocketClient.doHandshake(scalePlayerHandler,
                webSocketHttpHeaders,
                URI.create("ws://localhost:" + randomServerPort + "/play/scale")).get();
        TextMessage message = new TextMessage("Hello !!");
        webSocketSession.sendMessage(message);
        Assertions.assertTrue(webSocketSession.isOpen());
        log.info("sent message - " + message.getPayload());
    }

}
