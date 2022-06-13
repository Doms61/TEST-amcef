package com.dominik.intro.project.openapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping ("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
