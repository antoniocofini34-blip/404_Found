package com.example.business;
import java.util.*;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.Brano;
import com.example.domain.CriterioFiltro;
public interface RicercaService {
  List<Brano> cercaBrani(String testo) throws BusinessException;
  List<Album> cercaAlbum(String testo) throws BusinessException;
  List<Artista> cercaArtista(String testo) throws BusinessException;
  <T> List<T> filtra(List<T> elementi, CriterioFiltro<T> criterio) throws BusinessException;
}
