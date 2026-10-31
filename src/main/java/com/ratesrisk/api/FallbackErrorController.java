package com.ratesrisk.api;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Sends an unknown browser route back to the dashboard instead of Spring's Whitelabel page. */
@Controller
public class FallbackErrorController implements ErrorController {
    @RequestMapping("/error")
    public String redirectToDashboard() {
        return "redirect:/";
    }
}
