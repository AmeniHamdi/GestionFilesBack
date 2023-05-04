package com.example.csv.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.Long;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.ResponseMessage;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.DossierRepository;
import com.example.csv.services.DossierService;
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

class DossierControllerTest {




    @Autowired
    private MockMvc mvc;

    @MockBean
    private DossierService dossierService;
    //@Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    DossierRepository dossierRepository;

    @BeforeEach
    public void setUp() {

    }

    @Autowired
    private DossierController dossierController;
    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv", "text/csv", "some csv data".getBytes());

        ResponseEntity<ResponseMessage> response = dossierController.uploadFile(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Uploaded the file successfully: test.csv", response.getBody().getMessage());
    }

    @Test
    void uploadFileFailed() throws Exception {


// create a mock CSV file with some content
        String csvContent = "numero,Nom,siren,ref_mandat\n1,imen,@gmail.com,mzoughi\n2,ahmed,.@gmail.com,MZOUGHI\n";
        MultipartFile mockCsvFile = new MockMultipartFile("file","test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        doThrow(new RuntimeException("exception")).when(dossierService).saveFile(mockCsvFile);

        ResponseEntity<ResponseMessage> result = dossierController.uploadFile(mockCsvFile);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dossierService.saveFile(mockCsvFile);
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
        ResponseEntity<ResponseMessage> response = dossierController.uploadFile(file);

        // Check if the response has the correct status code and message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Please upload a csv file!", response.getBody().getMessage());
    }

    @Test
    void save() throws Exception {

            // Create a new dossier object
            Dossier dossier = new Dossier();
            dossier.setDossier_DC("Test Dossier");

            // Set up a mock file service that returns the same dossier object
            when(dossierService.save(any(Dossier.class))).thenReturn(dossier);

            // Send a POST request to the endpoint with the dossier object as the request body
            mvc.perform(post("/api/csv/dossier")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dossier)))
                    .andExpect(status().isOk());
//                    .andExpect(jsonPath("$.name", is(dossier.getDossier_DC()))); // Check that the response contains the same dossier object

            // Verify that the fileService.save() method was called with the dossier object
            verify(dossierService).save(eq(dossier));
        }


    @Test
    void getDossier() throws Exception {
        Long dossierId = 123L;

        // Create a mock Dossier object
        Dossier mockDossier = new Dossier();
        mockDossier.setId(dossierId);

        // Set up the mock behavior of the fileService.getDossier() method
        when(dossierService.getDossier(dossierId)).thenReturn(mockDossier);

        // Send a GET request to /{id} endpoint and expect a HTTP 200 response
        mvc.perform(get("/api/csv/dossier/{id}", dossierId))
                .andExpect(status().isOk());


        // Verify that the fileService.getDossier() method is called only once with the correct dossierId parameter
        verify(dossierService, times(1)).getDossier(dossierId);
        verifyNoMoreInteractions(dossierService);



    }

    @Test
    public void testDeleteDossier() {
        Long id = 1L;

        // mock the fileService

        when(dossierService.getDossier(id)).thenReturn(new Dossier(id));

        // create the controller instance
        DossierController controller = new DossierController(dossierService);

        // perform the delete operation
        ResponseEntity<Void> response = controller.deleteDossier(id);

        // assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // verify that the delete method of fileService was called with the correct id
        verify(dossierService, Mockito.times(1)).delete(id);
    }
    @Test
    void deleteDossier_noContent() throws Exception {
        Long dossierId = 3L;

        // Set up a mock file service that returns null for the specified dossier ID
        when(dossierService.getDossier(dossierId)).thenReturn(null);

        DossierController controller = new DossierController(dossierService);
        ResponseEntity<Void> response = controller.deleteDossier(dossierId);

        // assert the response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dossierService, Mockito.times(0)).delete(dossierId);
        // Send a GET request to the endpoint with the specified dossier ID

    }
//    @Test
//    void updateDossier() {
//    }

    @Test
    void getAllDossiers() throws Exception {

        mvc.perform(get("/api/csv/dossier")).andExpect(status().isOk());

    }

    @Test
    void getDossier_noContent() throws Exception {
        Long dossierId = 3L;

        // Set up a mock file service that returns null for the specified dossier ID
        when(dossierService.getDossier(dossierId)).thenReturn(null);

        // Send a GET request to the endpoint with the specified dossier ID
        mvc.perform(get("/api/csv/dossier/{id}", dossierId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
    @Test
    void getDossierWithSorting() throws Exception {
        // Create some Dossier objects
        Dossier dossier1 = new Dossier();
        dossier1.setId(1L);
        dossier1.setDossier_DC("Dossier 1");

        Dossier dossier2 = new Dossier();
        dossier2.setId(2L);
        dossier2.setDossier_DC("Dossier 1");

        // Set up the mock file service to return a list of the Dossier objects
        List<Dossier> dossiers = Arrays.asList(dossier1, dossier2);
        when(dossierService.findDossierWithSorting(anyString())).thenReturn(dossiers);

        // Send a GET request to the endpoint with a field parameter
        MockHttpServletRequestBuilder requestBuilder = get("/api/csv/dossier/get/{field}", "Dossier_DC")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify that the response contains the correct Dossier objects, sorted by the creation date field
        String responseContent = result.getResponse().getContentAsString();
        List<Dossier> responseDossiers = objectMapper.readValue(responseContent, new TypeReference<List<Dossier>>(){});
        assertEquals(2, responseDossiers.size());
        assertEquals(1L, responseDossiers.get(0).getId());
        assertEquals("Dossier 1", responseDossiers.get(0).getDossier_DC());
        assertEquals(2L, responseDossiers.get(1).getId());
        assertEquals("Dossier 1", responseDossiers.get(1).getDossier_DC());
    }
    @Test
    public void testUpdateDossier() {
        Dossier dossier = new Dossier();
        dossier.setId(1L);
        dossier.setListSDC("Test Dossier");

        Mockito.when(dossierService.update(dossier)).thenReturn(true);

        ResponseEntity<Void> response = dossierController.updateDossier(dossier);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateDossierNotFound() {
        Dossier dossier = new Dossier();
        dossier.setId(1L);
        dossier.setListSDC("Test Dossier");

        Mockito.when(dossierService.update(dossier)).thenReturn(false);

        ResponseEntity<Void> response = dossierController.updateDossier(dossier);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getContractsCount() throws Exception {
        mvc.perform(get("/api/csv/dossier/count")).andExpect(status().isOk());
    }
}