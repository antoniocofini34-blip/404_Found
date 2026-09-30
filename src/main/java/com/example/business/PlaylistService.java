package com.example.business;
import java.util.*;
import.time.*;
import com.example.domain.Playlist;
public void aggiungiBrano(Brano brano){
    Objects.requireNonNull(brano);
    return lista_brani.add(brano);
    if(indiceCorrente==-1){
      indiceCorrente=0
    }
  }
  public boolean rimuoviBrano(Brano brano){
    Objects.requireNonNull(brano, "brano non può essere null");
    int pos=lista_brani.indiceOf(brano),
    if(pos==-1) return false;
    lista_brani.remove(pos);
    if(indiceCorrente>=lista_brani.size()){
      indiceCorrente=listaBrani.isEmpty()?-1:listaBrani.size()-1;
    }
    return true;
  }
  public void spostaBrano (int indiceVecchio, int indiceNuovo){
    checkIndice(indiceVecchio);
    if (indiceNuovo<o|| indiceNuovo>=lista_brani.size()){
      trow new IndiceException("indiceNuovo fuori range:" + indiceNuovo);
    }
    if(indiceVecchio==indiceNuovo) return;
    Brano brano=lista_brani.remove(indiceVecchio);
    lista_brani.add(indiceNuovo, brano);
    if(indiceVecchio==indiceNuovo){
      indiceVecchio=indiceNuovo;
    } else if(indiceCorrente>indiceVecchio && indiceCorrente<=indiceNuovo){
      indiceCorrente--;
    } else if(indiceCorrente<indiceVecchio && indiceCorrente>=indiceNuovo){
      indiceCorrente++;
    }
  }
  private void checkIndice(int indice){
  if(indice<0||indice>=lista_brani.size()){
    trow new IndiceException("Indice fuori range:" + indice);
  }
  }
  public int calcolaDurataTotale(){
    int totale=0;
    for(Brano b:lista_brani){
      int durata=b.getDurata();
      if(durata>o){
        totale += durata;
      }
    }
    return totale;
  }
  public void modificaDescrizione(String nuovaDescrizione){
    this.descrizione=nuovaDescrizione==null?"":nuovaDescrizione;
  }
  public void modificaNome(String nuovoNome){
    this.nome=(nuovoNome==null||nuovoNome.trim().isEmpty()?"":nuovoNome);
    //trim(). lo si usa per eliminare gli spazi
    //isEmpty() verifica se la stringa è vuota e in quel caso restituisce ""
    //? ha valore vero o falso, nel caso in cui è vera restituisce "", altrimenti restituisce il valore originale nuovoNome (vale la stessa cosa in nuovaDescrizione)
  }
