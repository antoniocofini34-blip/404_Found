package com.example.business;
import java.util.*;
import.time.*;
import com.example.domain.Playlist;
public void aggiungiBranoPlaylist(Brano brano){
    Objects.requireNonNull(brano);
    if(indiceCorrente==-1){
      indiceCorrente=0
    }
    lista_brani.add(brano);
  }
  public void rimuoviBranoPlaylist(Brano brano){
    Objects.requireNonNull(brano, "brano non può essere null");
    int pos=lista_brani.indexOf(brano),
    if(pos>-1) 
        lista_brani.remove(pos);
    if(indiceCorrente>=lista_brani.size()){
      indiceCorrente=listaBrani.isEmpty()?-1:listaBrani.size()-1;
    }
  }
  public void riordinaBrano (int indiceVecchio, int indiceNuovo){
    checkIndice(indiceVecchio);
    if (indiceNuovo<0||indiceNuovo>=lista_brani.size()){
      trow new IllegalArgumentException("indiceNuovo fuori range:" + indiceNuovo);
    }
    if (indiceVecchio==indiceNuovo) return;
    Playlist brano_playlist=lista_brani.remove(indiceVecchio);
    lista_brani.add(indiceNuovo);
    if(indiceVecchio!=indiceNuovo){
        if (indiceCorrente>indiceVecchio && indiceCorrente<=indiceNuovo){
          indiceCorrente--;
    } else if(indiceCorrente<indiceVecchio && indiceCorrente>=indiceNuovo){
      indiceCorrente++;
        }
      } 
  }
  private void checkIndice(int indice){
  if(indice<0||indice>=lista_brani.size()){
    trow new IllegalArgumentException("Indice fuori range:" + indice);
  }
  }
  public int calcolaDurataTotale(){
    int totale=0;
    for(Brano b:lista_brani){
      int durata=b.getDurata();
      if(durata>0){
        totale += durata;
      }
    }
    return totale;
  }
