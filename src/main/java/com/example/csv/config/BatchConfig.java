package com.example.csv.config;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.example.csv.batch.DocumentProcessor;
import com.example.csv.batch.DocumentWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import com.example.csv.domain.Tiers;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class BatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private static final String FILE_NAME = "Tiers.csv";
    private static final String JOB_NAME = "listStocksJob";
    private static final String STEP_NAME = "processingStep";
    private static final String READER_NAME = "stockItemReader";



    private String names ="Numero,nom,siren,ref_mandat";

    private String delimiter = ",";



    @Bean
    public Job listStocksJob(Step step1) {
        log.info("STEP1"+" "+step1);
        return jobBuilderFactory.get(JOB_NAME).start(step1).build();
    }


    @Bean
    public Step stockStep() {
        return stepBuilderFactory.get(STEP_NAME).<Tiers, Tiers>chunk(4).reader(stockItemReader())
                .processor(stockItemProcessor()).writer(stockItemWriter()).build();
    }


    @Bean
    public ItemReader<Tiers> stockItemReader() {
        FlatFileItemReader<Tiers> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(FILE_NAME));
        reader.setName(READER_NAME);
        reader.setLinesToSkip(1);


        reader.setLineMapper(stockLineMapper());
        log.info("reaaaader"+reader);
        return reader;

    }



    @Bean
    public LineMapper<Tiers> stockLineMapper() {

        final DefaultLineMapper<Tiers> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(names.split(delimiter));
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            Tiers data = new Tiers();
            data.setSiren(fieldSet.readString(0));
            data.setNumero(fieldSet.readString(1));
            data.setNom(fieldSet.readString(2));
            data.setRef_mandat(fieldSet.readString(3));
            log.info("dataaaaa"+data);
            return data;
        });
        return defaultLineMapper;
    }


    @Bean
    public ItemProcessor<Tiers, Tiers> stockItemProcessor() {
        return new DocumentProcessor();
    }


    @Bean
    public ItemWriter<Tiers> stockItemWriter() {
        return new DocumentWriter();
    }
}
