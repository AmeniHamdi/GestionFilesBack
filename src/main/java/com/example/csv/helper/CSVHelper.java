package com.example.csv.helper;

import com.example.csv.domain.Contrat;
import com.example.csv.domain.Dossier;
import com.example.csv.domain.Tiers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                        csvRecord.get("Num_dossierKPS"),
                        csvRecord.get("Num_CP"),
                        csvRecord.get("Raison_Social"),
                        csvRecord.get("Id_Tiers"),
                        csvRecord.get("Num_DC"),
                        csvRecord.get("Num_SDC"),
                        csvRecord.get("Num_CIR"),
                        csvRecord.get("Num_SIREN"),
                        csvRecord.get("Ref_Collaborative"),
                        csvRecord.get("Code_Produit"),
                        csvRecord.get("Identifiant_de_offre_comm"),
                        csvRecord.get("Chef_de_File"),
                        csvRecord.get("Num_OVI"),
                        csvRecord.get("Num_RUM"),
                        csvRecord.get("TypeEnergie"),
                        csvRecord.get("Produit_Comm"),
                        csvRecord.get("Produit"),
                        csvRecord.get("Phase"),
                        csvRecord.get("Montant_pret")
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
            record.get("Montant_du_pres")
         );
    }


    public static Contrat ocrToContrats(Map<String, String> record) {
        return new Contrat(
            null,
            record.get("Num_dossierKPS"),
            record.get("Num_CP"),
            record.get("Raison_Social"),
            record.get("Id_Tiers"),
            record.get("Num_DC"),
            record.get("Num_SDC"),
            record.get("Num_CIR"),
            record.get("Num_SIREN"),
            record.get("Ref_Collaborative"),
            record.get("Code_Produit"),
            record.get("Identifiant_de_offre_comm"),
            record.get("Chef_de_File"),
            record.get("Num_OVI"),
            record.get("Num_RUM"),
            record.get("TypeEnergie"),
            record.get("Produit_Comm"),
            record.get("Produit"),
            record.get("Phase"),
            record.get("Montant_pret")
        );

    }

    public static Tiers ocrToTiers(Map<String, String> record) {
        return new Tiers(
             null,
            record.get("Numero"),
            record.get("nom"),
            record.get("siren"),
            record.get("ref_mandat")
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
                        csvRecord.get("ref_mandat")
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
                        csvRecord.get("Montant_du_pres")
                );

                dossiers.add(dossier);
            }

            return dossiers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }




}
