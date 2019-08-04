package submodule;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SubModule {
    public static void main(String[] args)
            throws IOException, TimeoutException {
        System.out.println("Hello, It is Consumer Process.");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("rabbitmq");
        factory.setPassword("1234");
        factory.setHost("192.168.0.104");
        String QueueName = "tmp-queue";
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclarePassive(QueueName);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties theProps,
                                       byte[] body) throws IOException {

                String message = new String(body, "UTF-8");

                Map<String, Object> header = theProps.getHeaders();
                String pubIP = header.get("pubIP").toString();
                String pubHostname = header.get("pubHostname").toString();

                System.out.println("Received --> "
                        + theProps.getTimestamp() + ":" +  pubHostname + ":" + pubIP +
                        " | "
                        + "Body : " + message + "");
            }
        };
        channel.basicConsume(QueueName, true , consumer);

    }
}
