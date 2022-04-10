package com.qsspy.chessevent.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("kafka")
@Data
@Slf4j
@Profile("!test")
public class KafkaConfiguration {
     private String bootstrapServers;
     private String allowAutoCreateTopicsConfig;
     private String autoOffsetResetConfig;
     private String connectionsMaxIdleMsConfig;
     private String maxPollIntervalMsConfig;

     @PostConstruct
     public void post() {
         log.info("Kafka configuration : {}", this);
     }

     public Map<String, Object> getDefaultConfig() {
         final Map<String, Object> configMap = new HashMap<>();
         configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
         configMap.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, allowAutoCreateTopicsConfig);
         configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);
         configMap.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, connectionsMaxIdleMsConfig);
         configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMsConfig);
         configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         return configMap;
     }
}
