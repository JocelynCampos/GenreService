package se.edugrade.genreservice;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.genreservice.dto.GenreRequestDTO;
import se.edugrade.genreservice.exceptions.DuplicateNameException;
import se.edugrade.genreservice.repositories.GenreRepository;
import se.edugrade.genreservice.services.GenreService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    GenreRepository genreRepo;

    @InjectMocks
    GenreService genreService;

    @Test
    void addGenre_throwsDuplicate_IfNameAlreadyExists() throws Exception {
        when(genreRepo.existsByNameIgnoreCase("Reggaeton")).thenReturn(true);
        assertThrows(DuplicateNameException.class, () -> genreService.create(new GenreRequestDTO("Reggaeton")));
        verify(genreRepo, never()).save(any());
    }

    @Test
    void removeGenre() throws Exception {
        when(genreRepo.existsByNameIgnoreCase("Reggaeton")).thenReturn(false);
        assertThrows(DuplicateNameException.class, () -> genreService.delete(1L));
        verify(genreRepo, never()).save(any());
    }
}
