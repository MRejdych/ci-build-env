package app;

import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
public class ApiController {
    private static final String QUEUE = "dummyQ";
    private static final String DATA_QUEUE = "dummyDataQ";
    private static final ConnectionFactory cf;

    static {
        cf = new ConnectionFactory();
        cf.setHost("localhost");
    }

    @PostMapping("/api/add")
    public void add(@RequestParam("data") String data) throws IOException, TimeoutException {
        try (Connection connection = cf.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE, false, false, false, null);
            channel.basicPublish("", QUEUE, null, data.getBytes());
        }
    }

    @GetMapping
    public List<String> getAll() throws IOException, TimeoutException {
        try (Connection connection = cf.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE, false, false, false, null);
            channel.basicPublish("", QUEUE, null, "GET".getBytes());

            GetResponse response = channel.basicGet(QUEUE, true);
            return Utils.bytesArrayToStringList(response.getBody());
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
        }
    }
}
