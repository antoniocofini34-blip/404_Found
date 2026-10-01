package com.example.business;
import java.util.*;
import time.*;
import com.example.domain.Playlist;

public interface PlaylistService {
    void creaPlaylist(Playlist playlist) throws BusinessException; 
    void modificaPlaylist(Playlist playlist) throws BusinessException; 
    void eliminaPlaylist(Playlist playlist) throws BusinessException; 
    List<Playlist> findAllPlaylist() throws BusinessException;
    Playlist findPlaylistByNome(String nome) throws BusinessException;
    void aggiungiBrano(Playlist playlist, Brano brano) throws BusinessException;
    void rimuoviBrano(Playlist playlist, Brano brano) throws BusinessException;
    void riordinaBrani(Playlist playlist, int indiceVecchio, int indiceNuovo) throws BusinessException;
    List<Brano> getBrani(Playlist playlist) throws BusinessException;
    int calcolaDurataTotale(Playlist playlist) throws BusinessException;
    void riproduciPlaylist(Playlist playlist) throws BusinessException;
}
    
