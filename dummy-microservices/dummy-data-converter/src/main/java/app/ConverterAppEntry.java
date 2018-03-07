package app;

import com.rabbitmq.client.*;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConverterAppEntry {
    private static final String QUEUE = "dummyQ";
    private static final String DATA_QUEUE = "dummyDataQ";
    private static final String STORAGE_QUEUE = "dummyStorageQ";
    private static final ConnectionFactory cf;

    static {
        ConfigLoader configLoader = new ConfigLoader();
        cf = new ConnectionFactory();
        cf.setHost(configLoader.rabbitmqHost);
        cf.setPort(configLoader.rabbitmqPort);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.queueDeclare(DATA_QUEUE, false, false, false, null);
        channel.queueDeclare(STORAGE_QUEUE, false, false, false, null);

        Consumer consumer = new DummyConsumer(channel);
        channel.basicConsume(QUEUE, true, consumer);
    }

    private static class DummyConsumer extends DefaultConsumer {
        DummyConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            String message = new String(body, "UTF-8");

            switch (message) {
                case "GET":
                    System.out.println("Error: GET msg should not be sent to DummyConverter!");
                    break;
                default:
                    getChannel().basicPublish("", STORAGE_QUEUE, null, SerializationUtils.serialize("Converted " + message));
                    System.out.println("Converted " + message);
                    break;
            }
        }
    }
}
