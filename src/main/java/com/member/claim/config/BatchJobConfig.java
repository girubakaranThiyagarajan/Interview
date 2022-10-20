package com.member.claim.config;

import com.member.claim.batch.ClaimProcessor;
import com.member.claim.batch.ClaimsFieldSetMapper;
import com.member.claim.batch.ItemCountListener;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.dto.ClaimsBatchRequest;
import com.member.claim.service.ClaimProcessService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;


@Configuration
public class BatchJobConfig {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    public BatchJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<ClaimsBatchRequest> claimItemReader(@Value("#{jobParameters['inputFilePath']}") String pathToFile) {
        FlatFileItemReader<ClaimsBatchRequest> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(pathToFile));
        DefaultLineMapper<ClaimsBatchRequest> claimLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"claimId", "policyHolderId", "dateOfService", "categoryId", "billedAmt", "status"});
        claimLineMapper.setLineTokenizer(tokenizer);
        claimLineMapper.setFieldSetMapper(new ClaimsFieldSetMapper());
        claimLineMapper.afterPropertiesSet();
        reader.setLineMapper(claimLineMapper);
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ClaimProcessResponse> claimProcessWriter(@Value("#{jobParameters['outPutFilePath']}") String pathToFile) {
        FlatFileItemWriter<ClaimProcessResponse> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(pathToFile));
        writer.setAppendAllowed(true);
        writer.setHeaderCallback(writer1 -> writer1.write("policyId,policyHolderId,dateOfService,category,subCategory,billedAmt,policyHolderPays,planPays,ruleUsed,indAccumulatedDed,famAccumulatedDed,errorCode,errorMessage,processMessage"));
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[]{"policyId", "policyHolderId", "dateOfService", "category", "subCategory", "billedAmt", "policyHolderPays", "planPays", "ruleUsed", "indAccumulatedDed", "famAccumulatedDed", "errorCode", "errorMessage", "processMessage"});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public Step step1(ClaimProcessor claimProcessor,
                      FlatFileItemReader<ClaimsBatchRequest> claimItemReader,
                      FlatFileItemWriter<ClaimProcessResponse> claimProcessWriter) {
        return stepBuilderFactory.get("step1")
                .<ClaimsBatchRequest, ClaimProcessResponse>chunk(10)
                .reader(claimItemReader)
                .processor(claimProcessor)
                .writer(claimProcessWriter)
                .listener(listener())
                .build();
    }

    @Bean
    public ClaimProcessor claimProcessor(ClaimProcessService claimProcessService) {
        return new ClaimProcessor(claimProcessService);
    }

    @Bean
    public Job job(ClaimProcessor claimProcessor,
                   FlatFileItemReader<ClaimsBatchRequest> claimItemReader,
                   FlatFileItemWriter<ClaimProcessResponse> claimProcessWriter) {
        return jobBuilderFactory.get("job")
                .start(step1(claimProcessor, claimItemReader, claimProcessWriter))
                .build();
    }

    @Bean
    public ItemCountListener listener() {
        return new ItemCountListener();
    }
}