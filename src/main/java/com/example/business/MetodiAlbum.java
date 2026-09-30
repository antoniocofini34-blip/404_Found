package com.example.business;
import java.util.*;
import com.example.domain.Album;
public int getDurataTotale() {
    int durataTotale=0; 
    for (Brano brano : lista_brani) {
      durataTotale += brano.getDurata(); 
    }
    return durataTotale; 
  }
