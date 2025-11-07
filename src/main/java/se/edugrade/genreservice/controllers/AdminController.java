package se.edugrade.genreservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.genreservice.dto.GenreRequestDTO;
import se.edugrade.genreservice.dto.GenreResponseDTO;
import se.edugrade.genreservice.services.GenreService;

@RestController
@RequestMapping("/edufy/v1/genre")
public class AdminController {

    private final GenreService genreService;

    public AdminController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/add")
    public ResponseEntity<GenreResponseDTO> addGenre(@RequestBody GenreRequestDTO genre) {
        var created = genreService.create(genre);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable Long id, @RequestBody GenreRequestDTO rq) {
        GenreResponseDTO updated = genreService.update(id, rq);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<GenreResponseDTO> removeGenre(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
