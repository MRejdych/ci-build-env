import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConverterAppEntry {
    private static final String QUEUE = "dummyQ";
    private static final String DATA_QUEUE = "dummyDataQ";
    private static final ConnectionFactory cf;

    static {
        cf = new ConnectionFactory();
        cf.setHost("localhost");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = cf.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE, false, false, false, null);

            Consumer consumer = new DummyConsumer(channel);

            channel.basicConsume(QUEUE, true, consumer);
        }
    }

    private static class DummyConsumer extends DefaultConsumer {
        DummyConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
                                   AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            String message = new String(body, "UTF-8");

            switch (message) {
                case "GET":
                    System.out.println("GET ALL!");
                    break;
                default:
                    System.out.println("Converted " + message);
                    break;
            }
        }
    }
}
