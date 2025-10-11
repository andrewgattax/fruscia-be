package it.aborti.fruscia.listeners;

import it.aborti.fruscia.modelGame.eventi.AppEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppEventoListener {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @EventListener
    public void sendUpdate(AppEvento appEvento) {
        simpMessagingTemplate.convertAndSend("/topic/partita/" + appEvento.getIdPartita(), appEvento);
    }


}
