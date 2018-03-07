package app;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
public class ApiController {
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

    @PostMapping("/api/add")
    public void add(@RequestParam("data") String data) throws IOException, TimeoutException {
        try (Connection connection = cf.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE, false, false, false, null);
            System.out.println("Publishing to converter: " + data);
            channel.basicPublish("", QUEUE, null, data.getBytes());
        }
    }

    @GetMapping("api/getAll")
    public List<String> getAll() throws IOException, TimeoutException {
        System.out.println("getAll");
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(DATA_QUEUE, false, false, false, null);
        channel.queueDeclare(STORAGE_QUEUE, false, false, false, null);
        channel.basicPublish("", STORAGE_QUEUE, null, SerializationUtils.serialize("GET"));

        GetResponse response = channel.basicGet(DATA_QUEUE, true);
        return SerializationUtils.deserialize(response.getBody());

    }
}
