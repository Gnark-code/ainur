package fr.gnark.sound.port.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Autowired
    private ScalePlayerHandler scalePlayerHandler;
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(scalePlayerHandler, "/play/scale").setAllowedOrigins("http://localhost:8080");
    }
}
