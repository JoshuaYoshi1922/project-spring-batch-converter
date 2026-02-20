package com.example.BatchApp;


import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class BatchAppApplication {

	@Bean
	protected FlatFileItemReader<String> reader() {
		return new FlatFileItemReaderBuilder<String>()
				.resource(new ClassPathResource("cptcodes.csv"))
				.name("cvs-reader")
				.lineMapper((line, lineNumber) -> line)
				.build();
	}

	@Bean
	protected FlatFileItemWriter<String> writer() {
		String fileLocation = "src/main/resources/processed_cptcodes.csv";
		return new FlatFileItemWriterBuilder<String>()
				.name("csv-writer")
				.resource(new FileSystemResource(fileLocation))
				.lineAggregator(item -> item) // Lambda to directly return the String
				.build();
	}

	@Bean
	protected Step maskingStep(JobRepository jobRepo, PlatformTransactionManager manager,
							   FlatFileItemReader<String> reader,
							   FlatFileItemWriter<String> writer) {
		return new StepBuilder("masking-step", jobRepo)
				.<String, String>chunk(3, manager)
				.reader(reader)
				.writer(writer)
				.build();
	}

	@Bean
	protected Job maskingJob(JobRepository jobRepository, Step maskingStep) {
		return new JobBuilder("masking-job", jobRepository)
				.start(maskingStep)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(BatchAppApplication.class, args);
	}

}
