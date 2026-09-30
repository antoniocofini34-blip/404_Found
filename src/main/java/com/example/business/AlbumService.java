package com.example.business;
import java.util.*;
import com.example.domain.Album;
import com.example.domain.Brano;

public interface AlbumService{
    List<Album>findAllAlbum() throws BusinessException;
    Album findAlbumByTitolo(String titolo) throws BusinessException;
    void creaAlbum(Album album) throws BusinessException;
    void modificeAlbum(Album almbum) throws BusinessException;
    void eliminaAlbum(Album album) throws BusinessException;
    void aggiungiBrano(Album album) throws BusinessException;
    void rimuoviBrano(Album album) throws BusinessException;
    void riordinaBrano(Album album, int indiceVecchio, int indiceNuovo) throws BusinessException;
    List<Brano> findBrano(Album album) throws BusinessException;
    int calcolaDurataTotale(Album album) throws BusinessException;
}
