package com.example.springbootazurequeues.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.QueueServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureQueueConfig {

    @Value("${app.azure.queue.name}")
    private String QUEUE_NAME;

    @Value("${app.azure.queue.connection.default-endpoints-protocol}")
    private String DEF_PROTOCOL;

    @Value("${app.azure.queue.connection.account-name}")
    private String ACC_NAME;

    @Value("${app.azure.queue.connection.account-key}")
    private String ACC_KEY;

    private static String CONNECTION_STRING;

    @Bean
    public void setConnectionString(){
        CONNECTION_STRING = "DefaultEndpointsProtocol=" + DEF_PROTOCOL + ";" +
                "AccountName=" + ACC_NAME + ";" +
                "AccountKey=" + ACC_KEY;

    }

    @Bean
    public QueueClient queueClient(){
        setConnectionString();
        return new QueueClientBuilder()
                .connectionString(CONNECTION_STRING)
                .queueName(QUEUE_NAME)
                .buildClient();
    }

    @Bean
    public QueueServiceClient queueServiceClient(){
        setConnectionString();
        return new QueueServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();
    }

}
