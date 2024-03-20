package org.otus.dzmitry.kapachou.highload.web.socket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketTimelineTestController {

    @GetMapping("/")
    public String defaultHomePage(Model model) {
        return "index";
    }

}
