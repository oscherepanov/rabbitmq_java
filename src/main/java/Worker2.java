import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class Worker2 {

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


    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");
      try {
        Thread.sleep(3000);
        //    deliveryTag - the tag from the received AMQP.Basic.GetOk or AMQP.Basic.Deliver
        //    multiple - true to acknowledge all messages up to and including the supplied delivery tag; false to acknowledge just the supplied delivery tag.
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    boolean autoAck = false;

    channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
  }
}
