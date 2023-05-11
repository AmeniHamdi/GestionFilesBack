package com.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Scheduler {

    @Autowired
    private BatchLauncher batchLauncher;
   
    @Scheduled(fixedDelay = 200000)
    public void perform() throws Exception {
        log.info("Batch programmé pour tourner toutes les 5 minutes");
        batchLauncher.run();
    }
}
