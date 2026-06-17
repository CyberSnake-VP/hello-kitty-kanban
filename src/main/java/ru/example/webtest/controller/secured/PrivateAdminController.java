package ru.example.webtest.controller.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;
import ru.example.webtest.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class PrivateAdminController {
    private final UserService userService;

    @GetMapping
    public String getManagementPage(Model model) {
        User user = userService.getCurrntUser();

        model.addAttribute("userName", user.getName());
        if (user.isSuperAdmin()) {
            List<User> candidateToDelete = userService.findAllByRoleIn(Arrays.asList(UserRole.USER, UserRole.ADMIN));
            List<User> candidateToUpgrade = candidateToDelete.stream()
                    .filter(User::isSimpleUser)
                    .toList();

            model.addAttribute("candidateToDelete", candidateToDelete);
            model.addAttribute("candidateToUpgrade", candidateToUpgrade);
        } else {
            List<User> candidateToDelete = userService.findAllByRoleIn(Collections.singleton(UserRole.USER));
            model.addAttribute("candidateToDelete", candidateToDelete);
        }

        return "private/admin/management-page";
    }

    @PostMapping("delete-user")
    public String deleteUser(@RequestParam long id) {
        Optional<User> toBeDeletedOptional = userService.findById(id);
        if (toBeDeletedOptional.isEmpty()) {
            return "redirect:/admin";
        }
        User userToBeDeleted = toBeDeletedOptional.get();
        User currentUser = userService.getCurrntUser();

        // проверка на удаление, нельзя удалить супер админа
        if (userToBeDeleted.isSuperAdmin()) {
            return "redirect:/admin";
        }
        // удаление админа может только супер админ
        if (userToBeDeleted.isAdmin() && !currentUser.isSuperAdmin()) {
            return "redirect:/admin";
        }

        userService.deleteById(id);
        return "redirect:/admin";
    }
}
