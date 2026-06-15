package ru.example.webtest.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;
import ru.example.webtest.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class PublicAuthorizationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        if(error != null) {
            model.addAttribute("isAuthenticationFailed", true);
        }
        return "public/authorization/login-page";
    }


    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "public/authorization/registration-page";
    }

    @PostMapping("/registration")
    public String getRegistrationPage(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      HttpServletRequest request) {
        // кодируем пароль
        String encodedPassword = passwordEncoder.encode(password);
        // создаем пользователя
        User user = new User(name, email, encodedPassword, UserRole.USER);
        // сохраняем пользователя
        userService.save(user);

        // автологин, наш метод который повторяет то что делает спринг секьюрити,
        // процесса аутентификации и авторизации
        forceAutoLogin(email, password, request);

        return "redirect:/account";
    }

    // автологин, чтобы при регистрации сразу входить в ресурс
    private void forceAutoLogin(String email, String password, HttpServletRequest request) {
       // создаем роли
        Set<SimpleGrantedAuthority> roles = Collections.singleton(UserRole.USER.toAuthority());
        // получаем аутентификацию
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, roles);
        // Устанавливаем для текущего запроса
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // сохраняем сессию
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
