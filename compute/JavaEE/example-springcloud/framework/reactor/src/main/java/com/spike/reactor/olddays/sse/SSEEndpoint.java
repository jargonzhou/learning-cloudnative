package com.spike.reactor.olddays.sse;

import com.spike.reactor.domain.Temperature;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping("/sse")
public class SSEEndpoint {
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping
    public SseEmitter events(HttpServletRequest request) {
        SseEmitter emitter = new SseEmitter();
        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
        return emitter;
    }

    @Async // 异步处理
    @EventListener
    public void listen(Temperature temperature) {
        List<SseEmitter> dead = new ArrayList<>();
        clients.forEach(sseEmitter -> {
            try {
                sseEmitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                dead.add(sseEmitter);
            }
        });
        dead.forEach(clients::remove);
    }
}
