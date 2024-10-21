package com.exa.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InicioController {

    @GetMapping("/inicio")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("inicio");
        return modelAndView;
    }
}
