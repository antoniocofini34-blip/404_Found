package com.example.business;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.Album;
public interface ArtistaService {
    List<Artista> findAllArtisti() throws BusinessException;
    Artista findArtistaByNome(String nome) throws BusinessException;
    void creaArtista(Artista artista) throws BusinessException;
    void modificaArtista(Artista artista) throws BusinessException;
    void eliminaArtista(Artista artista) throws BusinessException;
    void aggiungiComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException;
    void rimuoviComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException;
    }
