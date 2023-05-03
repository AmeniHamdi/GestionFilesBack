package com.example.csv.controllers;

import com.example.csv.domain.Dossier;
import com.example.csv.domain.GetAllType;
import com.example.csv.domain.ResponseMessage;
import com.example.csv.domain.Tiers;
import com.example.csv.DTO.TiersDTO;
import com.example.csv.helper.CSVHelper;
import com.example.csv.services.TiersService;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/csv/tier")
@AllArgsConstructor
@Slf4j
public class TiersController {

    @Autowired
    private KafkaTemplate<String, Map> kafkaTemplate;
    @Autowired
    private final TiersService fileService;

    private void sendMessage(String topicName, Map message) {
        ListenableFuture<SendResult<String, Map>> future = kafkaTemplate.send(topicName, message);
    }

    @CrossOrigin
    @PostMapping ("test-kafka")
    public ResponseEntity<ResponseMessage> sendToKafka(@RequestParam("file") MultipartFile file) throws IOException {
        String topic = "input";
        Map message = new HashMap();
        message.put("file", file.getBytes()); // {"file": [12, 78, 96]
        sendMessage(topic, message);
        String response = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(response));
    }

    @CrossOrigin
    @GetMapping("/Search")
    public List<Tiers> searchTiers(@RequestParam(required = false) String numero,
                                   @RequestParam(required = false) String nom,
                                   @RequestParam(required = false) String siren,
                                   @RequestParam(required = false) String refMandat) {
        return fileService.searchTiers(numero, nom, siren, refMandat);
    }

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file)  {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {

                fileService.saveFile(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                log.info(String.valueOf(e));
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
    @CrossOrigin
    @PostMapping
    public Tiers save(@RequestBody Tiers tiers){


       return fileService.save(tiers);

    }
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Tiers> getTiers(@PathVariable("id")Long id){
        Tiers tiers = fileService.getTiers(id);
        if(tiers.equals(null)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tiers, HttpStatus.OK);
    }
    @CrossOrigin
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteTiers(@PathVariable("id") Long id){
        if(fileService.getTiers(id)== null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        fileService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @CrossOrigin
    @PatchMapping("/edit")
    public ResponseEntity<Void> updateTiers(@RequestBody Tiers tiers){

        boolean updated = fileService.update(tiers);
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @CrossOrigin
    @GetMapping
    public ResponseEntity<GetAllType<Tiers>> getAllTiers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "") String searchTerm
    )
    {
        GetAllType<Tiers> data = fileService.getAllTiers(page, size, sortBy, asc, searchTerm);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @GetMapping("/count")
    public Long getTiersCount() {
        return fileService.countTiers();
    }

}









