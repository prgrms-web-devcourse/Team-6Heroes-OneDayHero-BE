package com.sixheroes.onedayheroapi.restdocs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestdocsController {

    @GetMapping("/docs")
    public String docs() {
        return "docs/index.html";
    }
}
