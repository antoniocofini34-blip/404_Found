package com.example.business;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.domain.Ascolto; 
import com.example.domain.Brano; 
import com.example.domain.CronologiaAscolti; 
public interface CronologiaAscoltiService {
  void svuotaCronologia(CronologiaAscolti cronologia) throws BusinessException;
  List<Ascolto> getAscolti(CronologiaAscolti cronologia) throws BusinessException;
  int calcolaTempoTotaleAscolto(CronologiaAscolti cronologia) throws BusinessException;
}
