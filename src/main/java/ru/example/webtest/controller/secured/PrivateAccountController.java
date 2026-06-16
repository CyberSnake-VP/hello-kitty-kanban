package ru.example.webtest.controller.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.webtest.entity.RecordStatus;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.dto.RecordsContainerDto;
import ru.example.webtest.service.RecordService;
import ru.example.webtest.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class PrivateAccountController {
    private final UserService userService;
    private final RecordService recordService;


    @GetMapping
    public String getMainPage(Model model,
                              @RequestParam(name = "filter", required = false) String filterMode) {
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("userName", container.userName());
        model.addAttribute("records", container.records());
        model.addAttribute("numberOfDoneRecords", container.numberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.numberOfActiveRecords());
        return "private/account-page";
    }

    @PostMapping("/add-record")
    public String addRecord(@RequestParam("title") String formTitle,
                            @RequestParam(name = "filter", required = false, defaultValue = "all") String filterMode) {
        recordService.saveRecord(formTitle);
        return "redirect:/account" +(filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

    @PostMapping("/make-record-done")
    public String makeRecordDone(@RequestParam("id") int id,
                                @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.updateRecordStatus(id, RecordStatus.DONE);
        return "redirect:/account" + (filterMode != null  && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

    @PostMapping("/delete-record")
    public String deleteRecord(@RequestParam("id") int id,
                               @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.deleteRecord(id);
        return "redirect:/account" + (filterMode != null  && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }
}
