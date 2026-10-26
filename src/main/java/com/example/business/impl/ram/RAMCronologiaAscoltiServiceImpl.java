package com.example.business.impl.ram;
import java.util.ArrayList;
import java.util.List;
import com.example.business.exception.BusinessException;
import com.example.business.CronologiaAscoltiService;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Ascolto;
import com.example.domain.Brano;
import com.example.domain.CronologiaAscolti;
public class RAMCronologiaAscoltiServiceImpl implements CronologiaAscoltiService {
    @Override
    public void svuotaCronologia(CronologiaAscolti cronologia) throws BusinessException{
       if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere vuoto");
       cronologia.setRegistroAscolti(new ArrayList<>());
    }
    @Override
    public List<Ascolto> getAscolti(CronologiaAscolti cronologia) throws BusinessException {
        if (cronologia==null) throw new OperazioneNonValidaException("La cronologia non può essere vuota");
        return new ArrayList<>(cronologia.getRegistroAscolti());
    }
    @Override
    public int calcolaTempoTotaleAscolto(CronologiaAscolti cronologia) throws BusinessException{
     if (cronologia == null) throw new OperazioneNonValidaException("La cronologia non può essere vuota");
     int tempoTotale=0;
     for(Ascolto ascolto: cronologia.getRegistroAscolti()){
       if(ascolto!=null) tempoTotale+=ascolto.getTempoBranoAscoltato();
     }
     return tempoTotale;
    }
  }
