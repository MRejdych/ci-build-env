package app;

import com.rabbitmq.client.*;
import org.springframework.util.SerializationUtils;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class StorageAppEntry {
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
        channel.queueDeclare(DATA_QUEUE, false, false, false, null);
        channel.queueDeclare(STORAGE_QUEUE, false, false, false, null);

        Consumer consumer = new DummyConsumer(channel);
        channel.basicConsume(STORAGE_QUEUE, true, consumer);
    }

    private static class DummyConsumer extends DefaultConsumer {
        DataSource ds = new DataSource();

        DummyConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            String message = String.valueOf(SerializationUtils.deserialize(body));

            switch (message) {
                case "GET":
                    List<String> data = ds.getAll();
                    getChannel().basicPublish("", DATA_QUEUE, null, SerializationUtils.serialize(data));
                    System.out.println("getAll: " + data);
                    break;
                default:
                    ds.store(message);
                    System.out.println("Stored msg: " + message);
                    break;
            }
        }
    }
}
