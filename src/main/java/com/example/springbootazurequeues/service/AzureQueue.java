package com.example.springbootazurequeues.service;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.models.PeekedMessageItem;
import com.azure.storage.queue.models.QueueItem;
import com.azure.storage.queue.models.QueueMessageItem;
import com.azure.storage.queue.models.QueueProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /**
     * Obtiene el primer mensaje de la cola.
     * @return el primer mensaje en la cola.
     */
    @Override
    public String peekQueueMessage() {
        PeekedMessageItem peekedMessageItem = queueClient.peekMessage();
        return peekedMessageItem.getBody().toString();
    }

    /**
     * Modifica un mensaje en la cola el parámetro <code>searchString</code> funciona como método de búsqueda y el
     * parámetro <code>updatedContetents</code> reemplazará el contenido anterior del mensaje.
     * @param searchString contenido del primer mensaje que tenga esta descripción
     * @param updatedContents el nuevo contenido que tendrá el mensaje
     */
    @Override
    public void updateQueueMessage(String searchString, String updatedContents) {
        QueueProperties queueProperties = queueClient.getProperties();
        for(QueueMessageItem message: queueClient.receiveMessages(queueProperties.getApproximateMessagesCount())){
            if(message.getBody().toString().contains(searchString)){
                queueClient.updateMessage(message.getMessageId(),
                        message.getPopReceipt(),
                        updatedContents,
                        null);
                break;
            }
        }
    }

    /**
     * Actualizará el contenido que se encuentra al final de la cola.
     * @param updatedContents El nuevo contenido del mensaje
     */
    @Override
    public void updateFirstQueueMessage(String updatedContents) {
        QueueMessageItem message = queueClient.receiveMessage();
        if(null != message){
            queueClient.updateMessage(message.getMessageId(),
                    message.getPopReceipt(),
                    updatedContents,
                    null);
        }
    }

    /**
     * Obtiene el número actual de mensajes en la cola.
     * @return el número de mensajes en la cola actual.
     */
    @Override
    public long getQueueLength() {
        QueueProperties properties = queueClient.getProperties();
        return properties.getApproximateMessagesCount();
    }

    /**
     * Elimina el primer mensaje en la cola.
     */
    @Override
    public void dequeueMessage() {
        QueueMessageItem message = queueClient.receiveMessage();
        if(null != message){
            queueClient.deleteMessage(message.getMessageId(),message.getPopReceipt());
        }
    }

    /**
     * Obtiene una lista de los queues existentes en el storage queue.
     * @return La lista de los queues existentes en el storage.
     */
    @Override
    public List<QueueItem> listQueues() {
        return queueServiceClient.listQueues().stream().toList();
    }

    /**
     * Elimina el queue.
     */
    @Override
    public void deleteMessageQueue() {
        queueClient.delete();
    }
}
