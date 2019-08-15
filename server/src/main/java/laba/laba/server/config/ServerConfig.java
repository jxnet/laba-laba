package laba.laba.server.config;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerConfig {

    private static final ServerConfig INSTANCE;

    private String kafkaBootstrapServer;
    private String kafkaClientId;
    private String kafkaKeySerializer;
    private String kafkaValueSerializer;
    private String topic;

    public ServerConfig(String kafkaBootstrapServer, String kafkaClientId, String kafkaKeySerializer, String kafkaValueSerializer, String topic) {
        this.kafkaBootstrapServer = kafkaBootstrapServer;
        this.kafkaClientId = kafkaClientId;
        this.kafkaKeySerializer = kafkaKeySerializer;
        this.kafkaValueSerializer = kafkaValueSerializer;
        this.topic = topic;
    }

    public String getKafkaBootstrapServer() {
        return kafkaBootstrapServer;
    }

    public String getKafkaClientId() {
        return kafkaClientId;
    }

    public String getKafkaKeySerializer() {
        return kafkaKeySerializer;
    }

    public String getKafkaValueSerializer() {
        return kafkaValueSerializer;
    }

    public String getTopic() {
        return topic;
    }

    public static ServerConfig getInstance() {
        return INSTANCE;
    }

    public Map<String, Object> asMapConfig() {
        Map<String, Object> map = asMapKafkaConfig();
        map.put("lala.laba.server.topic", topic);
        return map;
    }

    public Map<String, Object> asMapKafkaConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("laba.laba.server.kafka.bootstrap.servers", kafkaBootstrapServer);
        map.put("laba.laba.server.kafka.client.id", kafkaClientId);
        map.put("laba.laba.server.kafka.key.serializer", kafkaKeySerializer);
        map.put("laba.laba.server.kafka.value.serializer", kafkaValueSerializer);
        return map;
    }

    static {
        Logger logger = LoggerFactory.getLogger(ServerConfig.class);
        ServerConfig instance;
        Properties properties = new Properties();
        String propertiesFile = System.getProperty("laba.laba.profiles.active", "development");
        if (propertiesFile.equals("production")) {
            propertiesFile = "profiles/"+ propertiesFile +"/application.properties";
        } else {
            propertiesFile = "profiles/" + propertiesFile + "/application.properties";
        }
        try {
            properties.load(ServerConfig.class.getClassLoader().getResourceAsStream(propertiesFile));
            String kafkaBootstrapServer = properties.getProperty("laba.laba.server.kafka.bootstrap.servers");
            String kafkaClientId = properties.getProperty("laba.laba.server.kafka.client.id");
            String kafkaKeySerializer = properties.getProperty("laba.laba.server.kafka.key.serializer");
            String kafkaValueSerializer = properties.getProperty("laba.laba.server.kafka.value.serializer");
            String topic = properties.getProperty("lala.laba.server.topic");
            instance = new ServerConfig(kafkaBootstrapServer, kafkaClientId, kafkaKeySerializer, kafkaValueSerializer, topic);
        } catch (IOException e) {
            logger.error(e);
            instance = null;
        }
        INSTANCE = instance;
    }

}
