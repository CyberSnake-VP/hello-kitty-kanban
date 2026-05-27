package ru.example.webtest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.webtest.entity.dto.RecordsContainerDto;
import ru.example.webtest.service.RecordService;

@Controller
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getMainPage(Model model,
                              @RequestParam(name = "filter", required = false) String filterMode) {
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("records", container.records());
        model.addAttribute("numberOfDoneRecords", container.numberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.numberOfActiveRecords());
        return "main-page";
    }
}
