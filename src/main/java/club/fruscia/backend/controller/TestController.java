package club.fruscia.backend.controller;

import it.trinex.blackout.security.BlackoutUserPrincipal;
import it.trinex.blackout.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CurrentUserService<BlackoutUserPrincipal> currentUserService;

    @GetMapping("/name")
    public String name() {
        return currentUserService.getCurrentPrincipal().getFirstName();
    }

}
