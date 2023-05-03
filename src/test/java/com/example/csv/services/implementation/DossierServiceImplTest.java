package com.example.csv.services.implementation;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.ContratRepository;
import com.example.csv.repositories.DossierRepository;
import com.example.csv.services.ContratService;
import com.example.csv.services.DossierService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DossierServiceImplTest {
    @Mock
    private DossierRepository dossierRepo ;
    private DossierService dossierService ;

    @BeforeEach
    void setUp() {
        dossierService= new DossierServiceImpl(dossierRepo);
    }




    @Test
    void canAddDossier() {
        Dossier dossier = new Dossier(
                null,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn");

        //When
        dossierService.save(dossier);

        ArgumentCaptor<Dossier> dossierArgumentCaptor = ArgumentCaptor.forClass(Dossier.class);
        verify(dossierRepo).save(dossierArgumentCaptor.capture());
        Dossier capturedDossier = dossierArgumentCaptor.getValue();


        //then
        assertEquals(capturedDossier,dossier);

    }
    @Test
    void getDossier() {
        Dossier expectedDossier = new Dossier(
                31L,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn");

        //then
        when(dossierRepo.findById(31L)).thenReturn(Optional.of(expectedDossier));
        Dossier actualDossier = dossierService.getDossier(31L);
        assertEquals(expectedDossier, actualDossier);
        verify(dossierRepo).findById(31L);

        log.info(""+expectedDossier);
        log.info(""+actualDossier);


    }

    @Test
    void update() {


        Dossier dossier = new Dossier(
                31L,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn");

        //then
        boolean result = dossierService.update(dossier);
        Mockito.verify(dossierRepo).save(dossier);
        Assert.assertTrue(result);



    }
    @Test
    void delete() {

        Dossier dossier = new Dossier(
                null,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn");

        //then
        dossierService.delete(dossier.getId());

        Mockito.verify(dossierRepo).deleteById(dossier.getId());

    }



    @Test
    void saveFile() {


        // create some test data
        byte[] fileContent = "dossier_DC,Numero,ListSDC,N_DPS,Montant_du_pres\nhbvba,1,ikhog,ihynr,jxudn\nhbvba,1,ikhog,ihynr,jxudn\n".getBytes();
        MockMultipartFile file = new MockMultipartFile("file.csv", "file.csv", "text/csv", fileContent);

        // create a list of expected dossier objects
        List<Dossier> expectedDossiers = new ArrayList<>();
        expectedDossiers.add( new Dossier(
                null,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn"));

        expectedDossiers.add( new Dossier(
                null,
                "hbvba",
                "1",
                "ikhog",
                "ihynr",
                "jxudn"));

        // create a mock CSVHelper that returns the expected dossier objects
        CSVHelper csvHelper = Mockito.mock(CSVHelper.class);





        // call the method under test
        dossierService.saveFile(file);

        // verify that the dossierRepository.saveAll method was called with the expected dossier objects
        Mockito.verify(dossierRepo).saveAll(expectedDossiers);

    }

    @Test
    void testSaveFileWithIOException() throws Exception {
        // create a mock MultipartFile that throws an IOException when its input stream is accessed
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenThrow(new IOException("File could not be read"));

        // verify that a RuntimeException is thrown when the file is saved
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dossierService.saveFile(mockFile);
        });

        String expectedMessage = "File could not be read";
        String actualMessage = exception.getCause().getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
        @Test
    void getAllDossiers() {
            // create some test data
            Dossier dossier1 = new Dossier(32l, "hbvba", "1", "ikhog", "ihynr", "jxudn");
            Dossier dossier2= new Dossier(31l, "hbvba", "1", "ikhog", "ihynr", "jxudn");
            List<Dossier> expectedDossier = new ArrayList<>();
            expectedDossier.add(dossier1);
            expectedDossier.add(dossier2);
            // create a mock repository and configure it to return the test data
            DossierRepository mockRepo = mock(DossierRepository.class);
            when(mockRepo.findAll()).thenReturn(expectedDossier);

            // create a service instance with the mock repository
            DossierServiceImpl dossierService = new DossierServiceImpl(mockRepo);

            // call the method and verify the results
            List<Dossier> actualDossier = dossierService.getAllDossiers();
            assertEquals(expectedDossier, actualDossier);
            verify(mockRepo).findAll();
    }



}