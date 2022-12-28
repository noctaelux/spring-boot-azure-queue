package com.example.springbootazurequeues.runner;

import com.example.springbootazurequeues.service.AzureQueue;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AzureQueueRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(AzureQueueRunner.class);

    private final AzureQueue queue;

    @Override
    public void run(String... args) throws Exception {
        queue.createQueue();
        log.info("Se ha creado el queue de nombre {}",queue.getQueueName());

        //Se agregan mensajes a la cola
        queue.addQueueMessage("Hola mundo desde Spring boot - Azure queue");
        queue.addQueueMessage("Esta es una prueba desde spring boot.");

        //Se obtiene el primer mensaje de la cola y se elimina de la misma.
        log.info(queue.peekQueueMessage());
        queue.dequeueMessage();

        //Se obtiene el segundo mensaje de la cola y se elimina de la misma.
        log.info(queue.peekQueueMessage());
        queue.dequeueMessage();

        //Se agrega un mensaje a la cola y se muestra en el log.
        queue.addQueueMessage("Este mensaje será modificado.");
        log.info(queue.peekQueueMessage());

        //Obtiene el primer mensaje de la cola que contenga "será modificado" y se reemplazará por uno nuevo.
        //Posteriormente, se motrará en el log y se eliminará de la cola.
        queue.updateQueueMessage("será modificado","Este mensaje ha sido modificado");
        log.info(queue.peekQueueMessage());
        queue.dequeueMessage();

        //Imprime los mensajes existentes en la cola (debe mostrar 0).
        log.info("Mensajes en cola: "+queue.getQueueLength());

        //Agrega 2 mensajes en la cola.
        queue.addQueueMessage("Este mensaje se modificará");
        queue.addQueueMessage("Este mensaje no se modificará");

        //Imprime los mensajes existentes en la cola (debe mostrar 2).
        log.info("Mensajes en cola: "+queue.getQueueLength());

        //Actualiza el último mensaje en la cola con el mensaje proporcionado. NO SE RECOMIENDA
        queue.updateFirstQueueMessage("Este mensaje se modificó");

        //Muestra los últimos dos mensajes de la cola y los elimina uno a uno.
        log.info(queue.peekQueueMessage());
        queue.dequeueMessage();
        log.info(queue.peekQueueMessage());
        queue.dequeueMessage();

        //Imprimirá los nombres de todos los queues existentes en el storage queue
        queue.listQueues().forEach(i -> log.info(i.getName()));

        //Elimina el queue creado al principio.
        queue.deleteMessageQueue();

        log.info("FINISH");
    }
}
