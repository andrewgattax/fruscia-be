package club.fruscia.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // prefisso per i topic (dove i client fanno subscribe)
        config.enableSimpleBroker("/topic");

        // prefisso per i messaggi in ingresso (dove i client mandano i messaggi)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint WebSocket (con SockJS come fallback)
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}