import java.util.*;
package com.example.domain;

public class ArtistaSolista extends Artista {
  private String nome_arte;
  public Artista_Solista (String biografia, GenereMusicale genere_principale, Set<GenereMusicale> generi_secondari, List<Album> discografia, String nome_arte) {
    super(biografia, genere_principale, generi_secondari, discografia);
    this.nome_arte=nome_arte;
  }
  public String getNomeArte() {
    return nome_arte;
  }
  public void setNomeArte(String nome_arte) {
    this.nome_arte=nome_arte;
  }
}
