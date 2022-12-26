package com.example.springbootazurequeues.service;

import com.azure.storage.queue.QueueClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AzureQueue implements Queue {

    private final QueueClient queueClient;


    @Override
    public void createQueue() {

    }

    @Override
    public void addQueueMessage(String message) {

    }

    @Override
    public void peekQueueMessage() {

    }

    @Override
    public void updateQueueMessage(String searchString, String updatedContents) {

    }

    @Override
    public void updateFirstQueueMessage(String updatedContents) {

    }

    @Override
    public long getQueueLength() {
        return 0;
    }

    @Override
    public void dequeueMessage() {

    }

    @Override
    public List listQueues() {
        return null;
    }

    @Override
    public void deleteMessageQueue() {

    }
}
