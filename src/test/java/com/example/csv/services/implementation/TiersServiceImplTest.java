package com.example.csv.services.implementation;

import com.example.csv.domain.Tiers;
import com.example.csv.helper.CSVHelper;
import com.example.csv.repositories.TiersRepository;
import com.example.csv.services.TiersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Slf4j

@ExtendWith(MockitoExtension.class)
class TiersServiceImplTest {
    @Mock
    private TiersRepository tiersRepository;
    private TiersService tiersService;


    @BeforeEach
    void setUp() {
        tiersService= new TiersServiceImpl(tiersRepository);
    }



    @Test
    void saveTiers(){
        Tiers tier = new Tiers(
                null,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");

        //When
        tiersService.save(tier);
        ArgumentCaptor<Tiers> tiersArgumentCaptor = ArgumentCaptor.forClass(Tiers.class);
        verify(tiersRepository).save(tiersArgumentCaptor.capture());
        Tiers capturedTier = tiersArgumentCaptor.getValue();


        //then
        assertEquals(capturedTier,tier);
        System.out.println(capturedTier);
        System.out.println(tier);

    }
    @Test
    void saveFile() {



        // create some test data
        byte[] fileContent = "Numero,nom,siren,ref_mandat\n1,Inga,.@yopmail.com,chviz\n1,Inga,.@yopmail.com,chviz\n".getBytes();
        MockMultipartFile file = new MockMultipartFile("file.csv", "file.csv", "text/csv", fileContent);

        // create a list of expected tiers objects
        List<Tiers> expectedTiers = new ArrayList<>();
        expectedTiers.add( new Tiers(
                null,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz"));

        expectedTiers.add( new Tiers(
                null,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz"));

        // create a mock CSVHelper that returns the expected tiers objects
        CSVHelper csvHelper = Mockito.mock(CSVHelper.class);





        // call the method under test
        tiersService.saveFile(file);

        // verify that the tiersRepository.saveAll method was called with the expected tiers objects
        Mockito.verify(tiersRepository).saveAll(expectedTiers);

    }


    @Test
    void testSaveFileWithIOException() throws Exception {
        // create a mock MultipartFile that throws an IOException when its input stream is accessed
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenThrow(new IOException("File could not be read"));

        // verify that a RuntimeException is thrown when the file is saved
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tiersService.saveFile(mockFile);
        });

        String expectedMessage = "File could not be read";
        String actualMessage = exception.getCause().getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void getAllTiers() {
        // create some test data
        Tiers tier1 =new Tiers(
                31L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");
        Tiers tier2= new Tiers(
                32L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");
        List<Tiers> expectedTiers= new ArrayList<>();
        expectedTiers.add(tier1);
        expectedTiers.add(tier2);
        // create a mock repository and configure it to return the test data
    TiersRepository mockRepo = mock(TiersRepository.class);
        when(mockRepo.findAll()).thenReturn(expectedTiers);

        // create a service instance with the mock repository
        TiersServiceImpl tiersService1 = new TiersServiceImpl(mockRepo);

        // call the method and verify the results
        List<Tiers> actualTiers = tiersService1.getAllTiers();
        assertEquals(expectedTiers, actualTiers);
        verify(mockRepo).findAll();

    }



    @Test
    void getTiers() {
        Tiers expectedtier = new Tiers(
                31L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");


        //then
        when(tiersRepository.findById(31L)).thenReturn(Optional.of(expectedtier));
        Tiers actualTier = tiersService.getTiers(31L);
        assertEquals(expectedtier, actualTier);
        verify(tiersRepository).findById(31L);

        log.info(""+expectedtier);
        log.info(""+actualTier);


    }

    @Test
    void delete() {

        Tiers tier = new Tiers(
                31L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");


        //then
        tiersService.delete(tier.getId());

        Mockito.verify(tiersRepository).deleteById(tier.getId());
    }

    @Test
    void update() {
        Tiers tier = new Tiers(
                null,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz");


        //then
        boolean result = tiersService.update(tier);
        Mockito.verify(tiersRepository).save(tier);
        Assert.assertTrue(result);


    }
  /*  @Test
    public void testGetAllTiers() {
        List<Tiers> tiersList = new ArrayList<>();
        tiersList.add(new Tiers(
                1L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz"));
        tiersList.add(new Tiers(
                2L,
                "1",
                "Inga",
                ".@yopmail.com",
                "chviz"));

        Page<Tiers> pagedResult = new PageImpl<>(tiersList);

        Mockito.when(tiersRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagedResult);

        List<Tiers> result = (List<Tiers>) tiersService.getAllTiers(0, 2, "id");

        assertEquals(2, result.size());
        assertEquals("Inga", result.get(0).getNom());
        assertEquals("Inga", result.get(1).getNom());
    }*/

//    @Test
//    public void testSearchTiers() {
//        List<Tiers> tiersList = new ArrayList<>();
//        tiersList.add(new Tiers(
//                1L,
//                "1",
//                "Inga",
//                ".@yopmail.com",
//                "chviz"));
//        tiersList.add(new Tiers(
//                2L,
//                "1",
//                "Inga",
//                ".@yopmail.com",
//                "chviz"));
//        Specification<Tiers> specification = Specification.where(null);
//        specification = specification.and((root, query, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("nom"), "Inga"));
//
//
//        Mockito.when(tiersRepository.findAll(specification)).thenReturn(tiersList);
//
//        List<Tiers> result = tiersService.searchTiers(null, "Inga", null, null);
//
//        assertEquals(2, result.size());
//        assertEquals("Inga", result.get(0).getNom());
//        assertEquals("Inga", result.get(1).getNom());
//    }
//

}