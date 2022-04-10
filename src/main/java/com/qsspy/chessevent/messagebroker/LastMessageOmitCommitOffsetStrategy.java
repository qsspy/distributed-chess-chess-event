package com.qsspy.chessevent.messagebroker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;

import java.util.HashMap;
import java.util.Map;

/**
 * Performs commit of previously consumed message offset (consumed before the one that is passed to commit() method).
 * This strategy is used when we always want to be able to re-consume last message on topic.
 * For this strategy to work auto.offset.reset parameter with value 'earliest' must be set for topic consumer.
 */
@Component
@Slf4j
public class LastMessageOmitCommitOffsetStrategy implements CommitOffsetStrategy{

    private final Map<String, ReceiverOffset> topicNamesWithLastOffsets;

    public LastMessageOmitCommitOffsetStrategy() {
        this.topicNamesWithLastOffsets = new HashMap<>();
    }

    @Override
    public Mono<Void> commit(final ReceiverOffset offset, final String topicName) {
        final ReceiverOffset lastProcessedOffset = this.getFromCache(topicName);
        if(lastProcessedOffset == null) {
            this.putInCache(topicName, offset);
            return Mono.empty();
        }

        if(areOffsetsTheSame(lastProcessedOffset, offset)) {
            log.info("Last offset is same as current. Skipping commit.");
            return Mono.empty();
        }

        this.putInCache(topicName, offset);
        return lastProcessedOffset.commit();
    }

    private boolean areOffsetsTheSame(final ReceiverOffset firstOffset, final ReceiverOffset secondOffset) {
        return firstOffset.offset() == secondOffset.offset();
    }

    private synchronized void putInCache(final String topicName, final ReceiverOffset offset) {
        topicNamesWithLastOffsets.put(topicName, offset);
    }

    private synchronized ReceiverOffset getFromCache(final String topicName) {
        return topicNamesWithLastOffsets.get(topicName);
    }
}
