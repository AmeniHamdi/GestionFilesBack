package com.example.csv.components;

import com.example.csv.services.ContratService;
import com.example.csv.services.DossierService;
import com.example.csv.services.TiersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.util.SerializationUtils.serialize;

@Component
public class CustomKafkaListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    TiersService tiersService;

    @Autowired
    DossierService dossierService;

    @Autowired
    ContratService contratService;


    @MessageMapping("/kafka/response")
    public void sendSpecific(
            @Payload Map msg) throws Exception {
        Message<byte[]> message = new Message<>() {
            @Override
            public byte[] getPayload() {
                try {
                    return new ObjectMapper().writeValueAsString(msg).getBytes();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
        simpMessagingTemplate.send( "/kafka/response",  message);
    }

    @KafkaListener(topics = "response", groupId = "csv")
    public void listenGroupCSV(Map message) {

        try {
            this.sendSpecific(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String type = (String) message.get("type");
        Map<String, String> content = (Map<String, String>) message.get("content");

        switch (type) {
            case "tiers" -> tiersService.saveResultsFromOcr(content);
            case "dossier" -> dossierService.saveResultsFromOcr(content);
            case "contrat" -> contratService.saveResultsFromOcr(content);
        }

    }
}
