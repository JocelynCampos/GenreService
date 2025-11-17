package se.edugrade.genreservice.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.edugrade.genreservice.dto.GenreRequestDTO;
import se.edugrade.genreservice.dto.GenreResponseDTO;
import se.edugrade.genreservice.entities.Genre;
import se.edugrade.genreservice.exceptions.DuplicateNameException;
import se.edugrade.genreservice.repositories.GenreRepository;

import java.util.List;

@Service
@Transactional
public class GenreService implements GenreServiceInterface {

    public static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
    /***********CustomerController**************/




    /******CommonController*******/

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponseDTO> findAll() {
        List<Genre> genres = genreRepository.findAll();

        if (genres.isEmpty()) {
            logger.info("No genres found");
            return List.of();
        }
        return genres.stream()
                .map(genre -> new GenreResponseDTO(genre.getId(), genre.getName()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponseDTO findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        String trimmedName = name.trim();

        return genreRepository.findByNameIgnoreCase(trimmedName)
                .map(genre -> new GenreResponseDTO(genre.getId(), genre.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre with this name not found."));
    }


    /**********AdminController************/

    @Override
    @Transactional(readOnly = true)
    public GenreResponseDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is required");
        }
        return genreRepository.findById(id)
                .map(genre -> new GenreResponseDTO(genre.getId(), genre.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
    }


    @Override
    public GenreResponseDTO create(GenreRequestDTO rq) {
        var name = rq.name().trim();
        if (name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (genreRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateNameException("Genre", name);
        }
        Genre genre = new Genre();
        genre.setName(name);
        genre = genreRepository.save(genre);
        logger.info("Genre created: {}", name);
        return new GenreResponseDTO(genre.getId(), genre.getName());
    }

    @Override
    public GenreResponseDTO update(Long id, GenreRequestDTO rq) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));

        var raw = rq.name();
        if (raw == null || raw.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name for genre is required");
        }
        var name = raw.trim();
        if (!name.equals(genre.getName()) && genreRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre with this name already exists");
        }
        genre.setName(name);
        var saved  = genreRepository.save(genre);
        logger.info("Genre updated: {}", name);
        return new GenreResponseDTO(saved.getId(), saved.getName());
    }

    @Override
    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found");
        }
        genreRepository.deleteById(id);
        logger.info("Genre deleted: {}", id);
    }
}
