package se.edugrade.genreservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/edufy/v1/genre")
public class PingController {

    @GetMapping("/test")
    public String test() {
        return "Ping";
    }
}
