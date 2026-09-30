import java.util.*;
package com.example.domain;

public class ArtistaGruppo extends Artista implements Ricercabile {
  private String nome_gruppo;
  private List<Artista> lista_Membri;
  public ArtistaGruppo(String nome_gruppo, List<Artista> lista_Membri) {
    this.nome_gruppo=nome_gruppo;
    this.lista_Membri=new ArrayList<>();
  }
  public String getNomeGruppo() {
    return nome_gruppo;
  }
  public List<Artista> getLista() {
    return lista_membri;
  }
  public void setNomeGruppo(String nome_gruppo) {
    this.nome_gruppo=nome_gruppo;
  }
  public void setListMembri(List<Artista> lista_Membri) {
    this.lista_Membri=lista_Membri;
  }
}
