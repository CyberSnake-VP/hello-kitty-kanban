package ru.example.webtest.controller.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;
import ru.example.webtest.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/super-admin")
public class PrivedSuperAdminController {
    private final UserService userService;

    @PostMapping("/make-user-admin")
    public String makeUserAdmin(@RequestParam long id) {
        Optional<User> userToBeUpgradedOptional = userService.findById(id);
        if(userToBeUpgradedOptional.isEmpty()) {
            return "redirect:/admin";
        }

        User userToBeUpgraded = userToBeUpgradedOptional.get();
        if(userToBeUpgraded.isSuperAdmin()) return "redirect:/admin";

        userService.updateRole(id, UserRole.ADMIN);
        return "redirect:/admin";
    }
}
