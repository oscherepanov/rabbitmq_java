import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class Worker1 {

  private final static String QUEUE_NAME = "hello";
  private final static String SERVER_URL = "rabbitmq.poas45.ru";
  private final static String USERNAME = "user";
  private final static String PASSWORD = "vba34tu";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(SERVER_URL);
    factory.setUsername(USERNAME);
    factory.setPassword(PASSWORD);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    // queueDeclare​(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,​Object> arguments)
    // durable - если мы объявляем устойчивую очередь (очередь сохранится после перезапуска сервера)
    // exclusive - эксклюзиваная оцередь (ограниченная соединением).
    // autoDelete - удаляется, если не будет использоваться
    // arguments - аргументы для очередь.

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
}
