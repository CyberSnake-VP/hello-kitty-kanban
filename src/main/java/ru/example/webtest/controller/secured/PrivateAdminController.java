package ru.example.webtest.controller.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.example.webtest.entity.User;
import ru.example.webtest.service.UserService;

@Controller
@RequiredArgsConstructor
public class PrivateAdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String getManagementPage(Model model) {
        User user = userService.getCurrntUser();
        model.addAttribute("userName", user.getName());
        return "private/admin/management-page";
    }
}
