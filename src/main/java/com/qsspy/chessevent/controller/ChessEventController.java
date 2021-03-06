package com.qsspy.chessevent.controller;

import com.qsspy.chessevent.service.ChessEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/events/board")
@RequiredArgsConstructor
@Profile("!test")
public class ChessEventController {

    private final ChessEventService eventService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public Flux<String> getBoardEventStream(
            @RequestParam("gameTopicId") final UUID topicId,
            @RequestParam("userToken") final UUID userToken
    ) {
        log.info("Received request for board event stream for topicId {} and userToken {}", topicId, userToken);
        return eventService.getBoardEventStream(topicId, userToken);
    }
}
