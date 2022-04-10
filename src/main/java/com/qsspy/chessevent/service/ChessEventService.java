package com.qsspy.chessevent.service;

import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ChessEventService {

    /** Performs logic in order to obtain stream of messages from message broker.
     *
     * @param gameTopicId id of topic
     * @param userToken player token
     * @return stream of event messages
     */
    Flux<String> getBoardEventStream(final UUID gameTopicId, final UUID userToken);
}
