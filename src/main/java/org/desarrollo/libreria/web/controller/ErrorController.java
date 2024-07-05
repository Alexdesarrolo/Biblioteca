package org.desarrollo.libreria.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error_403")
    public String error() {
        return "error/error_403";
    }
    
}
