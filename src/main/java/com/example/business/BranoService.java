package com.example.business
import java.util.*;
import com.example.domain.Brano;
public void inserisciGenereBrano(Genere genere_Brano) {
    this.genere_Brano.add(genere_Brano);  
  }
  public void inserisciAlbumBrano(Album album_Brano) {
    this.album_Brano.add(album_Brano);  
  }
  public void incrementaAscolti() {
    this.numero_Ascolti ++;
  }
