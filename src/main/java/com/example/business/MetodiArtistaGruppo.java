package com.example.business;
import java.util.*,
import com.example.domain.ArtistaGruppo;
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
