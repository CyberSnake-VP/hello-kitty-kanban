package ru.example.webtest.controller.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;
import ru.example.webtest.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrivateAdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String getManagementPage(Model model) {
        User user = userService.getCurrntUser();
        model.addAttribute("userName", user.getName());
        if(user.getRole() == UserRole.ADMIN) {
            List<User> candidateToDelete = userService.findAllByRoleIn(Arrays.asList(UserRole.USER,  UserRole.ADMIN));
            model.addAttribute("candidateToDelete", candidateToDelete);
        } else {
            List<User> candidateToDelete = userService.findAllByRoleIn(Collections.singleton(UserRole.USER));
            model.addAttribute("candidateToDelete", candidateToDelete);
        }
        return "private/admin/management-page";
    }
}
