package app;

import java.util.Optional;

public class ConfigLoader {
    public final String rabbitmqHost;
    public final int rabbitmqPort;
    public final String redisUrl;

    public ConfigLoader() {
        rabbitmqHost = Optional.ofNullable(System.getenv("RABBITMQ_HOST")).orElse("127.0.0.1");
        rabbitmqPort = Optional.ofNullable(System.getenv("RABBITMQ_PORT")).map(Integer::valueOf).orElse(6379);
        redisUrl = Optional.ofNullable(System.getenv("REDIS_URL")).orElse("http://0.0.0.0:5672");
    }
}
