package com.example.business
import java.util.*;
import com.example.domain.Brano;
import com.example.domain.Album;
import com.example.domain.Genere;

public interface BranoService{
  List<Brano> findAllBrani() throws BusinessException;
  Brano findBranoByTitolo(String titolo) throws BusinessException;
  void creaBrano (Brano brano) throws BusinessException;
  void modificaBrano(Brano brano) throws BusinessException;
  void eliminaBrano(Brano brano) throws BusinessException;
  void assegnaAlbum(Album album, Brano brano) throws BusinessException;
  void modificaGenere(Brano brano, Set<Genere> generi) throws BusinessException;
  void aggiornaNumeroAscolti(Brano brano) throws BusinessException;
}
