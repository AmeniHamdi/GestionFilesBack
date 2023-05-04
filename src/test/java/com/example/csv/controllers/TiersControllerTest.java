package com.example.csv.controllers;

import com.example.csv.domain.ResponseMessage;
import com.example.csv.domain.Tiers;
import com.example.csv.services.TiersService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;


import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TiersControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(TiersControllerTest.class);


    private TiersController controller;

    @MockBean
    private TiersService service;



    @Autowired
    private MockMvc mvc;


    @BeforeEach
    void setUp(){

        controller= new TiersController(null,service);
    }



    @Test
    void uploadFileSuccesfully() throws Exception {

// create a mock CSV file with some content
        String csvContent = "Numero,nom,siren,ref_mandat\n1,imen,mzoughi,1234\n2,AHMED,.@gmail.com,MZOUGHI\n";
        MockMultipartFile mockCsvFile = new MockMultipartFile("file","test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/tier/upload").file(mockCsvFile))
                .andExpect(status().isOk())
                .andReturn();


        String expectedContent = "Uploaded the file successfully: test.csv";
        String actualContent = result.getResponse().getContentAsString();
        String val = JsonPath.read(actualContent, "$.message");

        assertEquals(expectedContent,val);

        logger.info("Expected : " + expectedContent);
        logger.info("Resultat : " + val);


    }
    @Test
    void uploadFileFailed() throws Exception {


// create a mock CSV file with some content
        String csvContent = "numero,Nom,siren,ref_mandat\n1,imen,@gmail.com,mzoughi\n2,ahmed,.@gmail.com,MZOUGHI\n";
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
        logger.info("Resultat : " + actualContent + " Response status : "+ result.getStatusCode());

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
        logger.info("Resultat : " + val);
    }

//    @Test
//    void save() throws Exception {
//        Tiers t = new Tiers(1L, "1", "Iheb", "iheb.cherif99@gmail.com", "cherif");
//        ObjectMapper mapper = new ObjectMapper();
//        String body = mapper.writeValueAsString(t);
//        MvcResult result = mvc.perform(post("/api/csv/tier").content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        String actualResult = result.getResponse().getContentAsString();
//        logger.info(body);
//        logger.info(actualResult);
//        assertEquals(body,actualResult);
//    }
//     @Test
//    void getAllTiers() throws Exception {
//        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
//        Tiers t2 = new Tiers(2L,"2","ahmed",".@gmail.com","tounsi");
//        List<Tiers> content = new ArrayList<>();
//        content.add(t1);
//        content.add(t2);
//        Page<Tiers> page = new PageImpl<>(content, PageRequest.of(0, 2),2);
//
//        GetAllType<Tiers> results= new GetAllType<>();
//        when(service.getAllTiers(0,2,null,true,null)).thenReturn(results);
//
//        ResponseEntity<GetAllType<Tiers>> result = controller.getAllTiers(0,2,null,true,null);
//
//        List<Tiers> actualResult = result.getBody().getRows();
//        assertEquals(actualResult,page.getContent());
//        logger.info(String.valueOf(actualResult));
//        logger.info(String.valueOf(result.getStatusCode()));
//
//        assertEquals(HttpStatus.OK,result.getStatusCode());
//
//
//    }

    @Test
    void getTiers() {
        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
        when(service.getTiers(1L)).thenReturn(t1);
        ResponseEntity<Tiers> result = controller.getTiers(1L);

        Tiers actualContent = result.getBody();
        logger.info(String.valueOf(actualContent));
        assertEquals(t1,actualContent);
        String actualStatus = result.getStatusCode().toString();
        logger.info(actualStatus);
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }

//    @Test
//    void getTiersFailed() {
//        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
//        when(service.getTiers(1L)).thenReturn(null);
//        ResponseEntity<Tiers> result = controller.getTiers(1L);
//
//        Tiers actualContent = result.getBody();
//        logger.info(String.valueOf(actualContent));
//        logger.info(String.valueOf(actualContent));
//
//
//
//        String actualStatus = result.getStatusCode().toString();
//        logger.info(actualStatus);
//
//        assertEquals(HttpStatus.NO_CONTENT,result.getStatusCode());
//    }


//    @Test
//    void searchTiers() {
//        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
//        Tiers t2 = new Tiers(2L,"2","iheb",".@gmail.com","iheb");
//        List<Tiers> list = new ArrayList<>(List.of(t1,t2));
//        when(service.searchTiers("iheb",null,null,null)).thenReturn(list);
//        List<Tiers> result = controller.searchTiers("iheb",null,null,null);
//
//        List<Tiers> actualResult = result;
//        assertEquals(actualResult,list);
//        logger.info(String.valueOf(actualResult));
//        logger.info(String.valueOf(result));
//        assertEquals(HttpStatus.OK,result);
//    }

    @Test
    void deleteTiers() {
        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
        when(service.getTiers(t1.getId())).thenReturn(t1);
        ResponseEntity<Void> result = controller.deleteTiers(t1.getId());
        assertEquals(HttpStatus.OK,result.getStatusCode());

    }

    @Test
    void deleteTiersFailed() {
        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
        when(service.getTiers(t1.getId())).thenReturn(null);
        ResponseEntity<Void> result = controller.deleteTiers(t1.getId());
        assertEquals(HttpStatus.NO_CONTENT,result.getStatusCode());

    }

//    @Test
//    void updateTiers() {
//        Tiers t1 = new Tiers(1L,"1","iheb",".@gmail.com","cherif");
//        TiersDTO dto = new TiersDTO();
//        dto.setNom("mohamed");
//        ResponseEntity<Void> result = controller.updateTiers(t1);
//        assertEquals(HttpStatus.OK,result.getStatusCode());
//    }
}