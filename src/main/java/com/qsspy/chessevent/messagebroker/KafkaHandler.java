package com.qsspy.chessevent.messagebroker;

import com.qsspy.chessevent.configuration.KafkaConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class KafkaHandler<T> implements MessageBrokerHandler{

    private static final String CLIENT_ID_PREFIX = "player-";
    private static final String GAME_ID_PREFIX = "game-";

    private final KafkaConfiguration configuration;
    private final CommitOffsetStrategy commitStrategy;

    @Override
    public Flux<String> subscribeToEventStream(final String topicName, final String accessToken) {

        final ReceiverOptions<String, String> options = ReceiverOptions.<String, String>create(buildConfigMap(topicName, accessToken)).subscription(Collections.singleton(topicName));
        final KafkaReceiver<String, String> receiver = new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE, options);
        return receiver.receive()
                .doOnNext(consumedRecord -> {
                    commitStrategy.commit(consumedRecord.receiverOffset(), topicName);
                    log.info("Received element for topicName {}, playerToken {}, VALUE : {}", topicName, accessToken, consumedRecord.value());
                })
                .map(ConsumerRecord::value);
    }

    private Map<String, Object> buildConfigMap(final String topicName, final String accessToken) {
        final Map<String, Object> configMap = new HashMap<>(configuration.getDefaultConfig());
        configMap.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_PREFIX + accessToken);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, GAME_ID_PREFIX + topicName);
        return configMap;
    }
}
