package uz.script.wincrm.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import uz.script.wincrm.security.blacklist.TokenBlacklistService;
import uz.script.wincrm.security.jwt.JwtService;


@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }

        String token = header.substring(7);

        long remainingTime = jwtService.getRemainingTime(token);

        blacklistService.blacklist(token, remainingTime);
    }
}
