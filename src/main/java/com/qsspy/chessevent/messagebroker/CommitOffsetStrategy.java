package com.qsspy.chessevent.messagebroker;

import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;

public interface CommitOffsetStrategy {

    /** Performs commit of kafka topic (partition) offset in the way that implementation provides
     *
     * @param accessToken access token of player
     * @return mono of commit task
     */
    Mono<Void> commit(final ReceiverOffset offset, final String accessToken);
}
