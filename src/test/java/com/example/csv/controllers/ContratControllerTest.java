package com.example.csv.controllers;


import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.ResponseMessage;
import com.example.csv.repositories.ContratRepository;
import com.example.csv.services.ContratService;

import org.hibernate.bytecode.enhance.spi.CollectionTracker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ContratControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private ContratService contratService;
    //@Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ContratRepository contratRepository;


    @Autowired
    private ContratController contratController;


    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv", "text/csv", "some csv data".getBytes());

        ResponseEntity<ResponseMessage> response = contratController.uploadFile(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Uploaded the file successfully: test.csv", response.getBody().getMessage());
    }

    @Test
    void uploadFileFailed() throws Exception {


// create a mock CSV file with some content
        String csvContent = "numero,Nom,siren,ref_mandat\n1,imen,@gmail.com,mzoughi\n2,ahmed,.@gmail.com,MZOUGHI\n";
        MultipartFile mockCsvFile = new MockMultipartFile("file","test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        doThrow(new RuntimeException("exception")).when(contratService).saveFile(mockCsvFile);

        ResponseEntity<ResponseMessage> result = contratController.uploadFile(mockCsvFile);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contratService.saveFile(mockCsvFile);
        });


        String expectedContent = "Could not upload the file: test.csv!";
        String actualContent = result.getBody().getMessage();
        assertEquals(expectedContent,actualContent);
        assertEquals(HttpStatus.EXPECTATION_FAILED,result.getStatusCode());


    }
    @Test
    public void testUploadFileNotCSV() throws Exception {
        // Create a mock MultipartFile with an invalid format
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

        // Create the controller and call the uploadFile method with the mock file
//        DossierController controller = new DossierController(fileService);
        ResponseEntity<ResponseMessage> response = contratController.uploadFile(file);

        // Check if the response has the correct status code and message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Please upload a csv file!", response.getBody().getMessage());
    }

    @Test
    void save() throws Exception {

        // Create a new contrat object
        Contrat contrat = new Contrat();
        contrat.setChef_de_File("new chef de file ");

        // Set up a mock file service that returns the same contrat object
        when(contratService.save(any(Contrat.class))).thenReturn(contrat);

        // Send a POST request to the endpoint with the contrat object as the request body
        mvc.perform(post("/api/csv/contrat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contrat)))
                .andExpect(status().isOk());

        // Verify that the contratService.save() method was called with the contrat object
        verify(contratService).save(eq(contrat));
    }


    @Test
    void getContract() throws Exception {
       Long ContratId = 1L;
        Contrat contrat = new Contrat();
        contrat.setId(ContratId);


        // Set up the mock behavior of the fileService.getDossier() method
        when(contratService.getContrat(ContratId)).thenReturn(contrat);

        // Send a GET request to /{id} endpoint and expect a HTTP 200 response
        mvc.perform(get("/api/csv/contrat/{id}", ContratId))
                .andExpect(status().isOk());


        // Verify that the fileService.getDossier() method is called only once with the correct dossierId parameter
        verify(contratService, times(1)).getContrat(ContratId);
        verifyNoMoreInteractions(contratService);



    }

    @Test
    public void testDeleteContract() {
        Long id = 1L;

        // mock the fileService

        when(contratService.getContrat(id)).thenReturn(new Contrat(id));

        // create the controller instance
        ContratController controller = new ContratController(contratService);

        // perform the delete operation
        ResponseEntity<Void> response = controller.deleteContrat(id);

        // assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // verify that the delete method of fileService was called with the correct id
        verify(contratService, Mockito.times(1)).delete(id);
    }
    @Test
    void deleteContract_noContent() throws Exception {
        Long contratId = 3L;

        // Set up a mock file service that returns null for the specified dossier ID
        when(contratService.getContrat(contratId)).thenReturn(null);

        ContratController controller = new ContratController(contratService);
        ResponseEntity<Void> response = controller.deleteContrat(contratId);

        // assert the response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(contratService, Mockito.times(0)).delete(contratId);
        // Send a GET request to the endpoint with the specified dossier ID

    }

    @Test
    void getAllContracts() throws Exception {

        mvc.perform(get("/api/csv/contrat")).andExpect(status().isOk());

    }

    @Test
    void getContrat_noContent() throws Exception {
        Long contratId= 3L;

        // Set up a mock file service that returns null for the specified dossier ID
        when(contratService.getContrat(contratId)).thenReturn(null);

        // Send a GET request to the endpoint with the specified dossier ID
        mvc.perform(get("/api/csv/contrat/{id}", contratId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
//    @Test
//    void getDossierWithSorting() throws Exception {
//        // Create some Dossier objects
//        Dossier dossier1 = new Dossier();
//        dossier1.setId(1L);
//        dossier1.setDossier_DC("Dossier 1");
//
//        Dossier dossier2 = new Dossier();
//        dossier2.setId(2L);
//        dossier2.setDossier_DC("Dossier 1");
//
//        // Set up the mock file service to return a list of the Dossier objects
//        List<Dossier> dossiers = Arrays.asList(dossier1, dossier2);
//        when(dossierService.findDossierWithSorting(anyString())).thenReturn(dossiers);
//
//        // Send a GET request to the endpoint with a field parameter
//        MockHttpServletRequestBuilder requestBuilder = get("/api/csv/dossier/get/{field}", "Dossier_DC")
//                .contentType(MediaType.APPLICATION_JSON);
//        MvcResult result = mvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Verify that the response contains the correct Dossier objects, sorted by the creation date field
//        String responseContent = result.getResponse().getContentAsString();
//        List<Dossier> responseDossiers = objectMapper.readValue(responseContent, new TypeReference<List<Dossier>>(){});
//        assertEquals(2, responseDossiers.size());
//        assertEquals(1L, responseDossiers.get(0).getId());
//        assertEquals("Dossier 1", responseDossiers.get(0).getDossier_DC());
//        assertEquals(2L, responseDossiers.get(1).getId());
//        assertEquals("Dossier 1", responseDossiers.get(1).getDossier_DC());
//    }
    @Test
    public void testUpdateContrat() {
        Contrat contrat = new Contrat();
        contrat.setId(1L);
        contrat.setProduit("new product");

        Mockito.when(contratService.update(contrat)).thenReturn(true);

        ResponseEntity<Void> response = contratController.updateContrat(contrat);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateDossierNotFound() {
        Contrat contrat = new Contrat();
        contrat.setId(1L);
        contrat.setProduit("new product");


        Mockito.when(contratService.update(contrat)).thenReturn(false);

        ResponseEntity<Void> response = contratController.updateContrat(contrat);



        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getContractsCount() throws Exception {
        mvc.perform(get("/api/csv/dossier/count")).andExpect(status().isOk());
    }

}