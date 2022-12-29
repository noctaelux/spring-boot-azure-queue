# Spring Boot - Azure Storage Queue
## Requerimientos
- Java v11+
- Maven v3.8.6+
- Azure Storage Account (opcional)
- Docker v20+
- Azurite (Docker container) (opcional)
- Microsoft Azure Storage Explorer v1.27+

## Instalación / Ejecución
### Azurite
**Si no contamos con acceso a un Azure Storage**, podemos emular localmente la funcionalidad de este servicio por medio
de Azurite, por lo que no se incurrirá en gastos y con la ventaja de ser una herramienta oficial soportada por Microsoft.

Para instalar Azurite:
```shell
docker run -p 10000:10000 -p 10001:10001 -p 10002:10002 \
mcr.microsoft.com/azure-storage/azurite
```

### Spring boot
Para esta prueba de concepto sólo necesitaremos agregar la dependencia de Azure Storage Queue.
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-queue</artifactId>
    <version>12.15.1</version>
</dependency>
```

Finalmente ejecutamos en consola:
```shell
mvn spring-boot:run
```

## Descripción
**Azure Storage Queue** es un sistema de mensajería parecido a Kafka/RabbitMQ pero a menor escala y funcionalidad más 
simplificada. Por lo que se recomienda su uso para cargas de trabajo no muy intensivas o poco complejas. Sin embargo la
plataforma permite una gama de personalización sobre el ciclo de vida de los mensajes. Por lo que es buena alternativa a
Redis si se desean mensajes con mayor duración dentro del Queue (hasta 7 días).

Esta prueba de conceptos está enfocada en la publicación y consumo de mensajes por medio de la librería de Azure Storage
Queue. Adicionalmente, se muestran algunas otras funcionalidades que también pueden ser de utilidad. El punto de partida
lo encontraremos en la clase `AzureQueueRunner` que se encuentra en el package _runner_, que puede contrastar un
RestController que permita realizar operaciones por medio de una interfaz HTTP y se encolen posteriormente en Azure
Storage Queue. El archivo de configuraciones es `AzureQueueConfig`.

A la fecha de este commit, el componente 'Azure Storage' que se provee al inicializar un nuevo proyecto de spring boot
se encuentra en su fase beta, por lo que no sugerimos aún su uso productivo. Alternativamente, en esta PoC sugerimos 
ocupar la librería oficial de Microsoft mostrada en la sección _Instalación/Ejecución_.

Para efectos de ahorro de gastos en cuentas Azure, sugerimos ocupar Azurite y Microsoft Azure Storage Explorer, para emular
el funcionamiento de un Azure Storage, por lo que tendremos acceso de forma local al blob, table y queues, sin incurrir
en gastos por uso de la plataforma.

**Microsoft Azure Storage Explorer** es una herramienta con interfaz gráfica amigable, que nos permitirá gestionar 
Azurite para crear tablas, blobs y queues. Para más información sobre estas dos herramientas, se sugiere seguir los 
siguientes enlaces: 

- Azurite https://learn.microsoft.com/es-es/azure/storage/common/storage-use-azurite.
- MS Azure Storage Explorer https://learn.microsoft.com/es-es/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-storage

En esta prueba se deja ambientado todo lo necesario para realizar pruebas de forma local con Azurite.

### Importante (cuando se trabaje con un Storage en Azure)
Si se tiene lo necesario para trabajar en la nube de Azure, incluso para configurar el espacio para ambientes más 
productivos, se deben hacer algunos cambios dentro del código.

Para ejecutar el proyecto localmente, en el archivo `application.properties` se configura el parámetro
```properties
app.azure.queue.connection.queue-endpoint=http://127.0.0.1:10001/devstoreaccount1
```
Mismo que ya no será necesario cuando se trabaje desde la nube. Por lo que deberemos **eliminar** este parámetro.

Como consecuencia de eliminar el parámetro descrito, también deberemos eliminar la constante `QUEUE_ENDPOINT` que se 
encuentra en la clase `AzureQueueConfig`, así como todas las referencias al mismo en el código.

El bean `setConnectionString` deberá quedar como sigue:
```java
@Bean
public void setConnectionString(){
    CONNECTION_STRING = "DefaultEndpointsProtocol=" + DEF_PROTOCOL + ";" +
    "AccountName=" + ACC_NAME + ";" +
    "AccountKey=" + ACC_KEY;
}
```

### Fuentes
- https://learn.microsoft.com/en-us/azure/storage/queues/storage-java-how-to-use-queue-storage
- https://learn.microsoft.com/es-es/azure/storage/common/storage-use-azurite
- https://learn.microsoft.com/es-es/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-storage