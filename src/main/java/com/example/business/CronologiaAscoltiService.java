package com.example.business;
import java.util.*; 
import com.example.domain.Ascolto; 
import com.example.domain.Brano; 
import com.example.domain.CronologiaAscolti; 
public interface CronologiaAscoltiService {
  void aggiungiAscolto(CronologiaAscolti cronologia, Ascolto ascolto) throws BusinessException;
  void rimuoviAscolto(CronologiaAscolti cronologia, Ascolto ascolto) throws BusinessException;
  void svuotaCronologia(CronologiaAscolti cronologia) throws BusinessException;
  List<Ascolto> getAscolti(CronologiaAscolti cronologia) throws BusinessException;
  int calcolaTempoTotaleAscolto(CronologiaAscolti cronologia) throws BusinessException;
  int contaAscolti(CronologiaAscolti cronologia, Brano brano) throws BusinessException;
}
