package com.example.domain;
import java.util.*;
import java.time.*;
public class Playlist implements Riproducibile{
  private List<Brano> lista_brani;
  private String nome;
  private String descrizione;
  private LocalDate data_creazione;
  private String proprietarioEmail; 
  public Playlist(String nome, String descrizione, LocalDate data_creazione, String proprietarioEmail){
    this.nome=nome;
    this.descrizione=descrizione;
    this.data_creazione=data_creazione;
    this.proprietarioEmail=proprietarioEmail;
  }
  public List<Brano> getListaBrani() { return new ArrayList<>(lista_brani); }
  public String getNome() { return nome; }
  public String getDescrizione() { return descrizione; }
  public LocalDate getDataCreazione() { return data_creazione; }
  public String getProprietarioEmail() { return proprietarioEmail; }
  public void setListaBrani(List<Brano> lista_brani) {
    if(lista_brani!=null) this.lista_brani=new ArrayList<>(lista_brani); 
    else this.lista_brani=new ArrayList<>();
    }
  public void setNome(String nome) { this.nome=nome; }
  public void setDescrizione(String descrizione) { this.descrizione=descrizione; }
}
