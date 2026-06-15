package ru.example.webtest.controller.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    // в случае когда ошибка будет раньше чем работа контроллера, мы меняем собственную реализация обработчика spring
    // просто редиректим в нужный для нас контроллер
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String redirectToSpecificToErrorPage(HttpServletResponse response) {
        return switch (HttpStatus.valueOf(response.getStatus())) {
            case NOT_FOUND -> "redirect:/error/404";
            case FORBIDDEN -> "redirect:/error/403";
            default -> "redirect:error/500";
        };
    }

    @RequestMapping(value = "/error/500")
    public String getCommonErrorPage() {
        return "public/error/common-error-page";
    }

    @RequestMapping(value = "/error/404")
    public String getNotFoundErrorPage() {
        return "public/error/not-found-error-page";
    }

    @RequestMapping(value = "/error/403")
    public String getForbiddenErrorPage() {
        return "public/error/forbidden-error-page";
    }


    @ExceptionHandler(Throwable.class)
    public String handleThrowable() {
        return "redirect:/error/500";
    }
}
