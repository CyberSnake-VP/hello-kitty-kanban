package ru.example.webtest.controller.secured;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class PrivateSessionController {

    @GetMapping("/session-info")
    public String getSessionInfoPage(Model model,
                                     HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", getRoles(auth));

        // false означает, если сессия существует, верни ее, иначе null
        HttpSession session = request.getSession(false);
        if(session != null) {
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("sessionCreated", new Date(session.getCreationTime()));
        }
        return "private/session-info-page";
    }

    private Collection<? extends GrantedAuthority> getRoles(Authentication auth) {
        return auth.getAuthorities().stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .collect(Collectors.toList());
    }
}
