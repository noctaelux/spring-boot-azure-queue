package com.example.springbootazurequeues.service;

import java.util.List;

public interface Queue {

    void createQueue();

    String getQueueName();

    void addQueueMessage(String message);

    String peekQueueMessage();

    void updateQueueMessage(String searchString, String updatedContents);

    void updateFirstQueueMessage(String updatedContents);

    long getQueueLength();

    void dequeueMessage();

    List<?>listQueues();

    void deleteMessageQueue();

}
