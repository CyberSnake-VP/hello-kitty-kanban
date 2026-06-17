package ru.example.webtest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;
import ru.example.webtest.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllByRoleIn(Collection<UserRole> roles) {
        return userRepository.findAllByRoleInOrderById(roles);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getCurrntUser() {
        // Извлекаем для текущего пользователя, из текущего http запроса
        // метод getAuthentication имеет роли(авторизация) логин и пароль
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(()-> new IllegalArgumentException("User with email = " + email + " not found"));
    }
}
