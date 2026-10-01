package com.example.domain;
import java.util.*;
import java.time.*;

public class Playlist implements Riproducibile{
  private List<Brano> lista_brani;
  private String nome;
  private String descrizione;
  private LocalDate data_creazione;
  private int indiceCorrente; 
  private int indiceNuovo; 
  private int indiceVecchio; 
  public Playlist(String nome, String descrizione, LocalDate data_creazione, int indiceCorrente, int indiceNuovo, int indiceVecchio){
    this.lista_brani=new ArrayList<>();
    this.nome=nome;
    this.descrizione=descrizione;
    this.data_creazione=data_creazione;
    this.indiceCorrente=-1;
    this.indiceNuovo=indiceNuovo; 
    this.indiceVecchio=indiceVecchio; 
  }
  public List<Brano> getLista_brani(){
    return new ArrayList<>(lista_brani);
  }
  public String getNome(){
    return nome;
  }
  public String getDescrizione(){
    return descrizione;
  }
  public LocalDate getData_creazione(){
    return data_creazione;
  }
  public int getIndiceCorrente(){
    return indiceCorrente;
  }
  public int getIndiceNuovo(){
    return indiceNuovo; 
  }
  public int getIndiceVecchio(){
    return indiceVecchio; 
  }
  public void setNome(String nome){
    this.nome=nome;
  }
  public void setDescrizione(String descrizione){
    this.descrizione=descrizione;
  }
  public void setData_creazione(LocalDate data_creazione){
    this.data_creazione=data_creazione;
  }
  public void setLista_brani(List<Brano> nuovaLista_brani){
    if(nuovaLista_brani!=null) this.lista_brani=new ArrayList<>(nuovaLista_brani); 
    else this.lista_brani=new ArrayList<>();
    }
  public void setIndiceCorrente(int indiceCorrente){
    if(indiceCorrente<-1||indiceCorrente>=lista_brani.size()){
      throw new IllegalArgumentException("Indice corrente non valido:" + indiceCorrente);
    }
    this.indiceCorrente=indiceCorrente; 
  }
  public void setIndiceNuovo(int indiceNuovo){
    this.indiceNuovo=indiceNuovo;
  }
  public void setIndiceVecchio(int indiceVecchio){
    this.indiceVecchio=indiceVecchio; 
  }
}
