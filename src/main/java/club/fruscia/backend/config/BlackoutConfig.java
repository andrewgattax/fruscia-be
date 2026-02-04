package club.fruscia.backend.config;

import it.trinex.blackout.security.BlackoutUserPrincipal;
import it.trinex.blackout.service.CurrentUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlackoutConfig {
    @Bean
    public CurrentUserService<BlackoutUserPrincipal>  currentUserService() {
        return new CurrentUserService<>();
    }
}
