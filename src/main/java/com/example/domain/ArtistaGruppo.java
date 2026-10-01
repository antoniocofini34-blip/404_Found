import java.util.*;
package com.example.domain;

public class ArtistaGruppo extends Artista implements Ricercabile {
  private String nome_gruppo;
  private List<Artista> lista_Membri;
  public ArtistaGruppo(String biografia, GenereMusicale genere_pricipale, Set<GenereMusicale> generi_secondari, List<Album> discografia, String nome_gruppo, List<Artista> lista_Membri) {
    super (biografia, genere_principale, generi_secondari, discografia);
    this.nome_gruppo=nome_gruppo;
    if (lista_Membri!=null) this.lista_Membri=new ArrayList<>(lista_Membri); 
    else this.lista_Membri=new ArrayList<>();    
  }
  public String getNomeGruppo() {
    return nome_gruppo;
  }
  public List<Artista> getListaMembri() {
    return new ArrayList<>(this.lista_Membri); 
  }
  public void setNomeGruppo(String nome_gruppo) {
    this.nome_gruppo=nome_gruppo;
  }
  public void setListaMembri(List<Artista> lista_Membri) {
    if (lista_Membri!=null) this.lista_Membri=new ArrayList<>(lista_Membri); 
    else this.lista_Membri=new ArrayList<>(); 
  }
}
