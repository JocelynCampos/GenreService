package se.edugrade.genreservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.edugrade.genreservice.entities.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByNameIgnoreCase(String genre);
    Optional<Genre> findByName(String genre);
}
