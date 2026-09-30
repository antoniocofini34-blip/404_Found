package com.example.business;
import java.util.*;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.Album;

public interface ArtistaService {
    List<Artista> findAllArtisti() throws BusinessException;
    Artista findArtistaByNome(String nome) throws BusinessException;
    void creaArtista(Artista artista) throws BusinessException;
    void modificaArtista(Artista artista) throws BusinessException;
    void eliminaArtista(Artista artista) throws BusinessException;
    List<Album> findAlbumArtista(Artista artista) throws BusinessException;
    void aggiungiComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException;
    void rimuoviComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException;
    List<Artista> findComponenti(ArtistaGruppo gruppo) throws BusinessException;
    }

