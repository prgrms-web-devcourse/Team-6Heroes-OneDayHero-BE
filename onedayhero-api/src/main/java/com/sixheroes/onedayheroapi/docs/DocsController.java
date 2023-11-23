package com.sixheroes.onedayheroapi.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/docs/index")
    public String docs() {
        return "/docs/index.html";
    }
}
