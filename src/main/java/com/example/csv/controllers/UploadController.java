package com.example.csv.controllers;

import com.example.csv.domain.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/csv/kafka")
@AllArgsConstructor
@Slf4j
public class UploadController {

    @Autowired
    private KafkaTemplate<String, Map> kafkaTemplate;


    private void sendMessage(String topicName, Map message) {
        ListenableFuture<SendResult<String, Map>> future = kafkaTemplate.send(topicName, message);
    }

    @CrossOrigin
    @PostMapping ("/upload")
    public ResponseEntity<ResponseMessage> sendToKafka(@RequestParam("file") MultipartFile file) throws IOException {
        String topic = "input";
        Map message = new HashMap();
        message.put("file", file.getBytes()); // {"file": [12, 78, 96]
        sendMessage(topic, message);
        String response = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(response));
    }

}
