package com.qsspy.chessevent.mapper;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TopicNameMapper {

    private static final String TOPIC_PREFIX = "chess";
    private static final String TOPIC_SUFFIX = "board";
    private static final String UNDERSCORE = "_";

    public static String toTopicName(final UUID topicId) {
        final String result =  "" +
                TOPIC_PREFIX +
                UNDERSCORE +
                topicId.toString().replace("-", "") +
                UNDERSCORE +
                TOPIC_SUFFIX;
        log.info("Resolved topic name : " + result);
        return result;
    }
}
