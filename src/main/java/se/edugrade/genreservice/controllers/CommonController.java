package se.edugrade.genreservice.controllers;


import jakarta.servlet.http.PushBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.edugrade.genreservice.dto.GenreResponseDTO;
import se.edugrade.genreservice.entities.Genre;
import se.edugrade.genreservice.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/edufy/v1/genre")
public class CommonController {

    private final GenreService genreService;

    public CommonController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GenreResponseDTO>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }
}
