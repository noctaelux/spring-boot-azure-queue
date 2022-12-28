package com.example.springbootazurequeues.runner;

import com.example.springbootazurequeues.service.Queue;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

@RequiredArgsConstructor
public class AzureQueueRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(AzureQueueRunner.class);

    private final Queue queue;

    @Override
    public void run(String... args) throws Exception {
        queue.createQueue();
        log.info("FINISH");
    }
}
