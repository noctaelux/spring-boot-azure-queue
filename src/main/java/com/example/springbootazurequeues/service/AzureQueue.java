package com.example.springbootazurequeues.service;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.models.PeekedMessageItem;
import com.azure.storage.queue.models.QueueItem;
import com.azure.storage.queue.models.QueueMessageItem;
import com.azure.storage.queue.models.QueueProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * Interactúa con Azure Queue Storage, las configuraciones y beans de esta clase se pueden consultar/configurar en la
 * clase AzureQueueConfig.
 */
@Service
@RequiredArgsConstructor
public class AzureQueue implements Queue {

    private final QueueClient queueClient;
    private final QueueServiceClient queueServiceClient;

    /**
     * <b>Crea un nuevo Queue.</b>
     * <p>El nombre se configura en el parámetro app.azure.queue.name dentro del archivo application.properties</p>
     */
    @Override
    public void createQueue() {
        queueClient.create();
    }

    /**
     * Regresa el nombre del queue actual (el de azure)
     * @return Retorna el nombre del queue
     */
    @Override
    public String getQueueName(){
        return queueClient.getQueueName();
    }

    /**
     * Agrega un nuevo mensaje al queue
     * @param message lo que se quiere enviar al queue
     */
    @Override
    public void addQueueMessage(String message) {
        queueClient.sendMessage(message);
    }

    @Override
    public String peekQueueMessage() {
        PeekedMessageItem peekedMessageItem = queueClient.peekMessage();
        return peekedMessageItem.getMessageText();
    }

    @Override
    public void updateQueueMessage(String searchString, String updatedContents) {
        QueueProperties queueProperties = queueClient.getProperties();
        for(QueueMessageItem message: queueClient.receiveMessages(queueProperties.getApproximateMessagesCount())){
            if(message.getMessageText().contains(searchString)){
                queueClient.updateMessage(message.getMessageId(),
                        message.getPopReceipt(),
                        updatedContents,
                        Duration.ofSeconds(3));
                break;
            }
        }
    }

    @Override
    public void updateFirstQueueMessage(String updatedContents) {
        QueueMessageItem message = queueClient.receiveMessage();
        if(null != message){
            queueClient.updateMessage(message.getMessageId(),
                    message.getPopReceipt(),
                    updatedContents,
                    Duration.ofSeconds(3));
        }
    }

    @Override
    public long getQueueLength() {
        QueueProperties properties = queueClient.getProperties();
        return properties.getApproximateMessagesCount();
    }

    @Override
    public void dequeueMessage() {
        QueueMessageItem message = queueClient.receiveMessage();
        if(null != message){
            queueClient.deleteMessage(message.getMessageId(),message.getPopReceipt());
        }
    }

    @Override
    public List<QueueItem> listQueues() {
        return queueServiceClient.listQueues().stream().toList();
    }

    @Override
    public void deleteMessageQueue() {
        queueClient.delete();
    }
}
