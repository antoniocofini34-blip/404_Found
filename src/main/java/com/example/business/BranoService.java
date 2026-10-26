package com.example.business;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.domain.Brano;
public interface BranoService{
  List<Brano> findAllBrani() throws BusinessException;
  Brano findBranoByTitolo(String titolo) throws BusinessException;
  void creaBrano (Brano brano) throws BusinessException;
  void modificaBrano(Brano brano) throws BusinessException;
  void eliminaBrano(Brano brano) throws BusinessException;
  void aggiornaNumeroAscolti(Brano brano) throws BusinessException;
}
