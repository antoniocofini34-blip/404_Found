package com.example.business;
import java.util.*;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.Album;

public interface ArtistaService {
    List<Artista> findAllArtisti() throws BusinessException;
    Artista findArtistaByNome(String nome) throws BusinessException;
    void creaArtista(Artista artista) throws BusinessException;
    void creaArtista(Artista artista) throws BusinessException;
    void modificaArtista(Artista artista) throws BusinessException;
    void eliminaArtista(Artista artista) throws BusinessException;
    List<Album> findAlbumArtista(Artista artista) throws BusinessException;
public void modificaBiografia(String biografia) {
    this.biografia=biografia;
  }
  public void setGenerePrincipale(Genere genere_principale) {
    this.genere_principale=genere_principale;
  }
  public void aggiungiGeneriSecondari(Genere generi_secondari) {
    this.generi_secondari.add(generi_secondari); 
  }
  public void rimuoviGeneriSecondari(Genere generi_secondari) {
    this.generi_secondari.remove(generi_secondari);
  }
  public void aggiungiAlbum(Album album) {
    this.album.add(album); 
  }
  public void rimuoviAlbum(Album album) {
    this.album.remove(album); 
  }

  void aggiungiComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessExceptions;
  void rimuoviComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException;
  List<Artista> findComponenti(ArtistaGruppo gruppo) throws BusinessException;
  
  @Override
  public boolean corrispondeA(String testo) {
    return nome_gruppo.toLowerCase().contains(testo.ToLowerCase());
  }
}

