package com.example.csv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.core.Ordered;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@Order(Ordered.HIGHEST_PRECEDENCE + 99)

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/kafka")
                .setAllowedOrigins("http://20.216.140.169/")
                .withSockJS();
    }

    /*@Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32000);
        container.setMaxBinaryMessageBufferSize(32000);
        return container;
    } */

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) throws IllegalArgumentException {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String jwtToken = accessor.getNativeHeader("token").get(0);
                    String username = "";
                    try {
                        username = jwtTokenUtil.extractUsername(jwtToken);
                    } catch (Exception e) {
                        System.err.println("username " + jwtTokenUtil + " noww error " + e.getMessage() + " " + jwtToken);
                        throw e;
                    }
                    System.err.println(username);

                    UserDetails userDetails = null;
                    try {
                        userDetails = userDetailService.loadUserByUsername(username);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        throw e;
                    }

                }
                return message;
            }
        });
    }

}
