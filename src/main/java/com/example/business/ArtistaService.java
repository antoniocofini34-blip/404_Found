package com.example.business;
import java.util.*;
import com.example.domain.Artista;
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

public void aggiungiComponente(Artista artista) {
    lista.Membri.add(artista);
  }
  public void rimuoviComponente(Artista artista) {
    lista_Membri.remove(artista);
  }
  public List<Artista> getComponenti() {
    return lista_Membri;
  }
  @Override
  public boolean corrispondeA(String testo) {
    return nome_gruppo.toLowerCase().contains(testo.ToLowerCase());
  }

