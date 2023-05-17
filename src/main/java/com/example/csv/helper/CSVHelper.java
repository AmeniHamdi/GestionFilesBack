package com.example.csv.helper;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.Tiers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<String> getCSVHeader(InputStream is) throws UnsupportedEncodingException {

        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader,CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<String> columnsHeader = new ArrayList<>();
            for(String header : csvParser.getHeaderMap().keySet()){
                columnsHeader.add(header);
            }
            return columnsHeader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static List<Contrat> csvToContrats(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Contrat> contrats = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Contrat contrat = new Contrat(
                        null,
                        csvRecord.get("NumCP"),
                        csvRecord.get("RaisonSocial"),
                        csvRecord.get("IdTiers"),
                        csvRecord.get("NumSIREN"),
                        csvRecord.get("Produit"),
                        csvRecord.get("Phase"),
                        new HashMap<>()
                );
                contrats.add(contrat);
            }

            return contrats;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static Dossier ocrToDossiers(Map<String, String> record) {
         return new Dossier(
            null,
            record.get("dossier_DC"),
            record.get("Numero"),
            record.get("ListSDC"),
            record.get("N_DPS"),
            record.get("Montant_du_pres"),
            new HashMap<>()
         );
    }


    public static Contrat ocrToContrats(Map<String, String> record) {
        return new Contrat(
            null,

            record.get("NumCP"),
            record.get("RaisonSocial"),
            record.get("IdTiers"),
            record.get("NumSIREN"),
            record.get("Produit"),
            record.get("Phase"),
                new HashMap<>()
        );

    }

    public static Tiers ocrToTiers(Map<String, String> record) {
        String[] headers = {"Numero", "nom", "siren", "ref_mandat",};
        Map<String, Object> rest = new HashMap<>();
        for (Map.Entry<String, String> item : record.entrySet()) {
            if (item.getKey() != null && !(Arrays.asList(headers).contains(item.getKey()))) {
                rest.put(item.getKey(), item.getValue());
            }
        }
        return new Tiers(
             null,
            record.get("Numero"),
            record.get("nom"),
            record.get("siren"),
            record.get("ref_mandat"),
                rest
        );
    }



    public static List<Tiers> csvToTiers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Tiers> tiers = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Tiers tier = new Tiers(
                        null,
                        csvRecord.get("Numero"),
                        csvRecord.get("nom"),
                        csvRecord.get("siren"),
                        csvRecord.get("ref_mandat"),
                        new HashMap<>()
                );

                tiers.add(tier);
            }

            return tiers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }


    public static List<Dossier> csvToDossiers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Dossier> dossiers = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Dossier dossier = new Dossier(
                        null,
                        csvRecord.get("dossier_DC"),
                        csvRecord.get("Numero"),
                        csvRecord.get("ListSDC"),
                        csvRecord.get("N_DPS"),
                        csvRecord.get("Montant_du_pres"),
                        new HashMap<>()
                );

                dossiers.add(dossier);
            }

            return dossiers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }




}
