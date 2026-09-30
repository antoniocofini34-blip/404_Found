package com.example.business;
import java.util.*;
import java.time.*;
import com.example.domain.Utente;
  public Playlist creaPlaylist(String nome, String descrizione) {
    Playlist playlist=new Playlist(nome,descrizione);
    playlist_Create.add(playlist);
    return playlist;
  }
  public void eliminaPlaylist(Playlist playlist) {
    playlist_Create.remove(playlist);
  }
  public void aggiungiPreferenza(Genere genere) {
    preferenze_musicali.add(genere);
  }
  public void rimuoviPreferenza(Genere genere) {
    preferenze_musicali.remove(genere);
  }
  public void avviaPlaylist(Playlist playlist) {
    playlist.play();
  }
  public void avviaBrano(Brano brano) {
    brano.play();
  }
  public List<Brano> ottieniSuggerimenti() {
    return new ArrayList<>();
  }
