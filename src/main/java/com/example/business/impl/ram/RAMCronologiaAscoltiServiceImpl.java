package com.example.business.impl.ram;
import java.util.ArrayList;
import java.util.List;
import com.example.business.BusinessException;
import com.example.business.CronologiaAscoltiService;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Ascolto;
import com.example.domain.Brano;
import com.example.domain.CronologiaAscolti;
public class RAMCronologiaAscoltiServiceImpl implements CronologiaAscoltiService {
  @Override
  public void aggiungiAscolto(CronologiaAscolti cronologia, Ascolto ascolto) throws BusinessException {
      if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere null");
      if (ascolto == null) throw new OperazioneNonValidaException("L'ascolto non può essere null");
      List<Ascolto> ascolti=cronologia.getRegistroAscolti();
      ascolti.add(ascolto);
      cronologia.setRegistroAscolti(ascolti);
  }
  @Override
  public void rimuoviAscolto(CronologiaAscolti cronologia, Ascolto ascolto) throws BusinessException{
     if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere null");
     if (ascolto == null) throw new OperazioneNonValidaException("L'ascolto non può essere null");
     List<Ascolto> ascolti=cronologia.getRegistroAscolti();
     if(!ascolti.remove(ascolto)) throw new OperazioneNonValidaException("L'ascolto non è presente nella cronologia");
    @Override
    public void svuotaCronologia(CronologiaAscolti cronologia) throws BusinessException{
       if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere null");
       cronologia.setRegistroAscolti(new ArrayList<>());
    }
    @Override
        public List<Ascolto> getAscolti(CronologiaAscolti cronologia) throws BusinessException {
        if (cronologia==null) throw new OperazioneNonValidaException("La cronologia non può essere null");
        return new ArrayList<>(cronologia.getRegistroAscolti());
    }
    @Override
    public int calcolaTempoTotaleAscolto(CronologiaAscolti cronologia) throws BusinessException{
      if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere null");
     int tempoTotale=0
     for(Ascolto ascolto: cronologia.getRegistroAscolti()){
       if(ascolto!=null) tempoTotale+=ascolto.getTempoBranoAscoltato();
     }
     return tempoTotale;
    }
    @Override
    public int contaAscolti(CronologiaAscolti cronologia, Brano brano) throws BusinessException{
      if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere null");
      if(brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
      int conteggio=0
      for(Ascolto ascolto: cronologia.getRegistroAscolti()) {
        if(ascolto!=null && ascolto.getBrano().equals(brano)) conteggio++;
      }
      return conteggio;
    }
  }
