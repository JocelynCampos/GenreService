package se.edugrade.genreservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.genreservice.dto.GenreResponseDTO;
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

    @GetMapping("/by-name/{name}")
    public ResponseEntity<GenreResponseDTO> findByName(@PathVariable String name) {
        return ResponseEntity.ok(genreService.findByName(name));
    }
}
