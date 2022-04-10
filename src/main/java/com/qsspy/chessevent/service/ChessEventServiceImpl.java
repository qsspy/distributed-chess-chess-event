package com.qsspy.chessevent.service;

import com.qsspy.chessevent.mapper.TopicNameMapper;
import com.qsspy.chessevent.messagebroker.MessageBrokerHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@Profile("!test")
public class ChessEventServiceImpl implements ChessEventService{

    private final MessageBrokerHandler messageBrokerHandler;

    public ChessEventServiceImpl(
            final MessageBrokerHandler messageBrokerHandler) {
        this.messageBrokerHandler = messageBrokerHandler;
    }

    @Override
    public Flux<String> getBoardEventStream(final UUID gameTopicId, final UUID userToken) {
        final String topicName = TopicNameMapper.toTopicName(gameTopicId);
        return messageBrokerHandler.subscribeToEventStream(topicName, userToken.toString());
    }
}
