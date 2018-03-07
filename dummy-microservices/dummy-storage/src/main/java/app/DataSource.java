package app;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private RedissonClient client;
    private List<String> dummyStore;



    public DataSource() {
        Config config = new Config();
        ConfigLoader configLoader = new ConfigLoader();
        config.useSingleServer().setAddress(configLoader.redisUrl);

        client = Redisson.create(config);
        dummyStore = client.getList("dummyStore", new SerializationCodec());
    }

    public void store(String data) {
        dummyStore.add(data);
    }

    public List<String> getAll() {
        return new ArrayList<>(dummyStore);
    }
}
