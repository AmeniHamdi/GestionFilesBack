
package com.example.csv.controllers;

import com.example.csv.domain.ResponseMessage;
import com.example.csv.domain.Tiers;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.TiersRepository;
import com.example.csv.services.TiersService;
import com.example.csv.services.implementation.TiersServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;


@Slf4j
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TiersControllerTest {



    private TiersController controller;
    @MockBean
    private TiersService service;


    @Autowired
    private MockMvc mockMvc;
    private List<Tiers> tierList;
    @BeforeEach
    void setUp(){

        this.tierList = new ArrayList<>();
        this.tierList.add(new Tiers(1L,"t12","tier1","s214","red55"));
        this.tierList.add(new Tiers(2L,"t22","tier2","s300","tr58"));
        this.tierList.add(new Tiers(3L,"t33","tier3","s258","po849"));
    }

    @Test
    void shouldFetchAllTiers() throws Exception {

        given(service.getAllTiers()).willReturn(tierList);

        this.mockMvc.perform(get("/api/csv/tiers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(tierList.size())));
    }













    /* @Test
    void uploadFileSuccesfully() throws Exception {

        // create a mock CSV file with some content
        String csvContent = "Numero,nom,siren,ref_mandat\n 1,tier,@gmail.com,tier\n 2,tier2,.@gmail.com,tier2\n";
        MockMultipartFile mockCsvFile = new MockMultipartFile("file","test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        //doNothing().when(service).saveFile(mockCsvFile);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/tier/upload").file(mockCsvFile))
                .andExpect(status().isOk())
                .andReturn();


        String expectedContent = "Uploaded the file successfully: test.csv";
        String actualContent = result.getResponse().getContentAsString();
        String val = JsonPath.read(actualContent, "$.message");

        assertEquals(expectedContent,val);

        logger.info("Expected : " + expectedContent);
        logger.info("Resultat  : " + val);


    }
    @Test
    void uploadFileFailed() throws Exception {


        // create a mock CSV file with some content
        String csvContent = "numero,Nom,siren,ref_mandat\n1,tier1,@gmail.com,tier2\n2,tier3,.@gmail.com,tier4\n";
        MultipartFile mockCsvFile = new MockMultipartFile("file","test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        doThrow(new RuntimeException("exception")).when(service).saveFile(mockCsvFile);

        ResponseEntity<ResponseMessage> result = controller.uploadFile(mockCsvFile);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.saveFile(mockCsvFile);
        });


        String expectedContent = "Could not upload the file: test.csv!";
        String actualContent = result.getBody().getMessage();
        assertEquals(expectedContent,actualContent);
        assertEquals(HttpStatus.EXPECTATION_FAILED,result.getStatusCode());

        logger.info("Expected : " + expectedContent + " Response status : "+ HttpStatus.EXPECTATION_FAILED);
        logger.info("Resultat  : " + actualContent + " Response status : "+ result.getStatusCode());

    }

    @Test
    void uploadFileWrongFormat() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes());
        MvcResult result = mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/tier/upload").file(file))
                .andExpect(status().isBadRequest())
                .andReturn();


        String expectedContent = "Please upload a csv file!";
        String actualContent = result.getResponse().getContentAsString();
        String val = JsonPath.read(actualContent, "$.message");

        assertEquals(expectedContent,val);

        logger.info("Expected : " + expectedContent);
        logger.info("Resultat  : " + val);
    }




*/
}

