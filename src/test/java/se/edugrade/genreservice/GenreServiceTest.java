package se.edugrade.genreservice;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.edugrade.genreservice.dto.GenreRequestDTO;
import se.edugrade.genreservice.dto.GenreResponseDTO;
import se.edugrade.genreservice.entities.Genre;
import se.edugrade.genreservice.exceptions.DuplicateNameException;
import se.edugrade.genreservice.repositories.GenreRepository;
import se.edugrade.genreservice.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    GenreRepository genreRepo;

    @InjectMocks
    GenreService genreService;

    private static Genre genre(Long id, String name) {
        Genre g = new Genre();
        g.setId(id);
        g.setName(name);
        return g;
    }

    //Find All genres
    @Test
    void findAll_ReturnsAllGenres() {
        when(genreRepo.findAll())
                .thenReturn(List.of(genre(1L, "Bachata"), genre(2L, "Salsa")
                ));
        List<GenreResponseDTO> result = genreService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Bachata", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("Salsa", result.get(1).name());
        verify(genreRepo).findAll();
    }

    @Test
    void findAll_ReturnsEmptyList() {
        when(genreRepo.findAll()).thenReturn(List.of());
        List<GenreResponseDTO> result = genreService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(genreRepo).findAll();
    }

    //Create
    @Test
    void create_success_savesAndReturnsDTO() {
        var rq = new GenreRequestDTO("Reggaeton");
        when(genreRepo.existsByNameIgnoreCase("Reggaeton")).thenReturn(false);
        when(genreRepo.save(any(Genre.class))).thenAnswer(inv -> {
           Genre g = inv.getArgument(0);
           g.setId(10L);
           return g;
        });

        GenreResponseDTO dto = genreService.create(rq);

        assertEquals(10L, dto.id());
        assertEquals("Reggaeton", dto.name());
        verify(genreRepo).existsByNameIgnoreCase("Reggaeton");
        verify(genreRepo).save(argThat(g -> "Reggaeton".equals(g.getName())));

    }

    @Test
    void create_blankName_throwsBadRequest() {
        var rq = new GenreRequestDTO("  ");
        var example = assertThrows(ResponseStatusException.class, () -> genreService.create(rq));
        assertEquals(HttpStatus.BAD_REQUEST, example.getStatusCode());
        verifyNoInteractions(genreRepo);
    }

    @Test
    void create_duplicateGenreName_throwsDuplicateNameException() {
        when(genreRepo.existsByNameIgnoreCase("Reggaeton")).thenReturn(true);
        assertThrows(DuplicateNameException.class,
                ()-> genreService.create(new GenreRequestDTO("Reggaeton")));
        verify(genreRepo, never()).save(any());
    }

    //Update
     @Test
    void update_success_changeName() {
        var existingGenre = genre(5L, "Rock");
        when(genreRepo.findById(5L)).thenReturn(Optional.of(existingGenre));
        when(genreRepo.existsByNameIgnoreCase("Hard Rock")).thenReturn(false);
        when(genreRepo.save(any(Genre.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = genreService.update(5L, new GenreRequestDTO("Hard Rock"));

        assertEquals(5L, dto.id());
        assertEquals("Hard Rock", dto.name());

        verify(genreRepo).findById(5L);
        verify(genreRepo).existsByNameIgnoreCase("Hard Rock");
        verify(genreRepo).save(argThat(g -> g.getId().equals(5L) && "Hard Rock".equals(g.getName())));
    }

    @Test
    void update_conflict_whenNameExists() {
        when(genreRepo.findById(8L)).thenReturn(Optional.of(genre(8L, "Merengue")));
        when(genreRepo.existsByNameIgnoreCase("Cumbia")).thenReturn(true);

        var ex = assertThrows(ResponseStatusException.class, ()-> genreService.update(8L, new GenreRequestDTO("Cumbia")));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(genreRepo, never()).save(any());
    }



    //Remove
    @Test
    void removeGenre() throws Exception {
        when(genreRepo.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> genreService.delete(1L));

        verify(genreRepo).existsById(1L);
        verify(genreRepo, never()).deleteById(anyLong());
        verify(genreRepo, never()).save(any());
    }

    @Test
    void create_and_findById_success() {
        var rq = new GenreRequestDTO("Salsa");
        when(genreRepo.existsByNameIgnoreCase("Salsa")).thenReturn(false);
        when(genreRepo.save(any(Genre.class))).thenAnswer(inv -> {
            Genre g = inv.getArgument(0);
            g.setId(1L);
            return g;
        });

        when(genreRepo.findById(1L)).thenReturn(Optional.of(genre(1L, "Salsa")));
        GenreResponseDTO created = genreService.create(rq);

        GenreResponseDTO found = genreService.findById(1L);

        assertEquals(1L, created.id());
        assertEquals("Salsa", created.name());

        assertEquals(1L, found.id());
        assertEquals("Salsa", found.name());

        verify(genreRepo).existsByNameIgnoreCase("Salsa");
        verify(genreRepo).save(argThat(g -> "Salsa".equals(g.getName())));
        verify(genreRepo).findById(1L);
    }

    @Test
    void create_and_findByName_success() {
        var rq = new GenreRequestDTO("Bachata");
        when(genreRepo.existsByNameIgnoreCase("Bachata")).thenReturn(false);
        when(genreRepo.save(any(Genre.class))).thenAnswer(inv -> {
            Genre g = inv.getArgument(0);
            g.setId(2L);
            return g;
        });

        when(genreRepo.findByNameIgnoreCase("Bachata")).thenReturn(Optional.of(genre(2L, "Bachata")));

        GenreResponseDTO created = genreService.create(rq);
        GenreResponseDTO found = genreService.findByName(" Bachata ");

        assertEquals(2L, created.id());
        assertEquals("Bachata", created.name());
        assertEquals(2L, found.id());
        assertEquals("Bachata", found.name());
        verify(genreRepo).existsByNameIgnoreCase("Bachata");
        verify(genreRepo).save(argThat(g -> "Bachata".equals(g.getName())));
        verify(genreRepo).findByNameIgnoreCase("Bachata");
    }
}
