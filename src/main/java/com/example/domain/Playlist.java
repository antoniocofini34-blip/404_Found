package com.example.domain;
import java.util.*;
import java.time.*;
public class Playlist implements Riproducibile{
  private List<Brano> lista_brani;
  private String nome;
  private String descrizione;
  private LocalDate data_creazione;
  private int indice_corrente; 
  private int indice_nuovo; 
  private int indice_vecchio; 
  public Playlist(List<Brano> lista_brani, String nome, String descrizione, LocalDate data_creazione, int indice_corrente, int indice_nuovo, int indice_vecchio){
    if (lista_brani!=null) this.lista_brani=new ArrayList<>(lista_brani); 
    else this.lista_brani=new ArrayList<>(); 
    this.nome=nome;
    this.descrizione=descrizione;
    this.data_creazione=data_creazione;
    this.indice_corrente=-1;
    this.indice_nuovo=indice_nuovo; 
    this.indice_vecchio=indice_vecchio; 
  }
  public List<Brano> getListaBrani() { return new ArrayList<>(lista_brani); }
  public String getNome() { return nome; }
  public String getDescrizione() { return descrizione; }
  public LocalDate getDataCreazione() { return data_creazione; }
  public int getIndiceCorrente() { return indice_corrente; }
  public int getIndiceNuovo() { return indice_nuovo; }
  public int getIndiceVecchio() { return indice_vecchio; }
  public void setListaBrani(List<Brano> lista_brani) {
    if(lista_brani!=null) this.lista_brani=new ArrayList<>(lista_brani); 
    else this.lista_brani=new ArrayList<>();
    }
  public void setNome(String nome) { this.nome=nome; }
  public void setDescrizione(String descrizione) { this.descrizione=descrizione; }
  public void setDataCreazione(LocalDate data_creazione) { this.data_creazione=data_creazione; }
  public void setIndiceCorrente(int indice_corrente) {
    if(indiceCorrente < -1 || indice_corrente >= lista_brani.size()) throw new IllegalArgumentException("Indice corrente non valido:" + indice_corrente);
    this.indice_corrente=indice_corrente; 
  }
  public void setIndiceNuovo(int indice_nuovo) { this.indice_nuovo=indice_nuovo; }
  public void setIndiceVecchio(int indice_vecchio) { this.indice_vecchio=indice_vecchio; }
}
