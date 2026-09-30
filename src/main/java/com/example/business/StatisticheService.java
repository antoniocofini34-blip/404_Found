package com.example.business;
import java.util.*;
import com.example.domain.Artista;
import com.example.domain.Brano;
import com.example.domain.GenereMusicale;
import com.example.domain.Utente;
public interface StatisticheService {
  List<Brano> ottieniBraniPiuAscoltati(Utente utente, int limite) throws BusinessException;
  List<Artista> ottieniArtistiPiuAscoltati(Utente utente) throws BusinessException;
  GenereMusicale ottieniGenereMusicalePiuAscoltato(Utente utente) throws BusinessException;
  int calcolaTempoTotaleAscolto(Utente utente) throws BusinessException;
  List<Brano> suggerisciBrani(Utente utente) throws BusinessException;
}
