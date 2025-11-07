package se.edugrade.genreservice.services;

import se.edugrade.genreservice.dto.GenreRequestDTO;
import se.edugrade.genreservice.dto.GenreResponseDTO;


import java.util.List;

public interface GenreServiceInterface {
   List<GenreResponseDTO> findAll();
   GenreResponseDTO findById(Long id);
   GenreResponseDTO findByName(String name);
   GenreResponseDTO create(GenreRequestDTO rq);
   GenreResponseDTO update(Long id, GenreRequestDTO rq);
   void delete(Long id);
}
