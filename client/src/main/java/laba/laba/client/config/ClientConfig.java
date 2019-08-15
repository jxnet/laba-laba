package laba.laba.client.config;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClientConfig {

    private static final ClientConfig INSTANCE;

    private String kafkaBootstrapServer;
    private String kafkaClientId;
    private String kafkaKeySerializer;
    private String kafkaValueSerializer;
    private String kafkaGroupId;
    private String topic;

    public ClientConfig(String kafkaBootstrapServer, String kafkaClientId, String kafkaKeySerializer, String kafkaValueSerializer, String kafkaGroupId, String topic) {
        this.kafkaBootstrapServer = kafkaBootstrapServer;
        this.kafkaClientId = kafkaClientId;
        this.kafkaKeySerializer = kafkaKeySerializer;
        this.kafkaValueSerializer = kafkaValueSerializer;
        this.kafkaGroupId = kafkaGroupId;
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

    public String getKafkaGroupId() {
        return kafkaGroupId;
    }

    public String getTopic() {
        return topic;
    }

    public Collection<String> getTopics() {
        String topics = getTopic();
        if (!topics.contains(",")) {
            return Arrays.asList(topics);
        }
        return Arrays.asList(topics.split(","));
    }

    public static ClientConfig getInstance() {
        return INSTANCE;
    }

    public Map<String, Object> asMapConfig() {
        Map<String, Object> map = asMapKafkaConfig();
        map.put("lala.laba.client.topic", topic);
        return map;
    }

    public Map<String, Object> asMapKafkaConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("laba.laba.client.kafka.bootstrap.servers", kafkaBootstrapServer);
        map.put("laba.laba.client.kafka.client.id", kafkaClientId);
        map.put("laba.laba.client.kafka.key.serializer", kafkaKeySerializer);
        map.put("laba.laba.client.kafka.value.serializer", kafkaValueSerializer);
        map.put("laba.laba.client.kafka.group.id", kafkaGroupId);
        return map;
    }

    static {
        Logger logger = LoggerFactory.getLogger(ClientConfig.class);
        ClientConfig instance;
        Properties properties = new Properties();
        String propertiesFile = System.getProperty("laba.laba.profiles.active", "development");
        if (propertiesFile.equals("production")) {
            propertiesFile = "profiles/"+ propertiesFile +"/application.properties";
        } else {
            propertiesFile = "profiles/" + propertiesFile + "/application.properties";
        }
        try {
            properties.load(ClientConfig.class.getClassLoader().getResourceAsStream(propertiesFile));
            String kafkaBootstrapServer = properties.getProperty("laba.laba.client.kafka.bootstrap.servers");
            String kafkaClientId = properties.getProperty("laba.laba.client.kafka.client.id");
            String kafkaKeySerializer = properties.getProperty("laba.laba.client.kafka.key.serializer");
            String kafkaValueSerializer = properties.getProperty("laba.laba.client.kafka.value.serializer");
            String kafkaGroupId = properties.getProperty("laba.laba.client.kafka.group.id");
            String topic = properties.getProperty("lala.laba.client.topic");
            instance = new ClientConfig(kafkaBootstrapServer, kafkaClientId, kafkaKeySerializer, kafkaValueSerializer, kafkaGroupId, topic);
        } catch (IOException e) {
            logger.error(e);
            instance = null;
        }
        INSTANCE = instance;
    }

}
