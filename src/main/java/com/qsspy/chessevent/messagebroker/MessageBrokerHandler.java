package com.qsspy.chessevent.messagebroker;

import reactor.core.publisher.Flux;

public interface MessageBrokerHandler{

    /** Subscribes to the given topic and produces message stream which can be subscribed to.
     *
     * @param topicName name of the topic
     * @param accessToken player token
     * @return stream of kafka message broker messages associated with given topic
     */
    Flux<String> subscribeToEventStream(final String topicName, final String accessToken);
}
