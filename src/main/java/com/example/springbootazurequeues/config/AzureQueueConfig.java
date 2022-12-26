package com.example.springbootazurequeues.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureQueueConfig {

    @Value("{app.azure.queue.name}")
    private static String QUEUE_NAME;

    @Value("{app.azure.connection.string}")
    private static String CONNECTION_STRING;

    @Bean
    public QueueClient queueClient(){
        return new QueueClientBuilder()
                .connectionString(CONNECTION_STRING)
                .queueName(QUEUE_NAME)
                .buildClient();
    }

}
