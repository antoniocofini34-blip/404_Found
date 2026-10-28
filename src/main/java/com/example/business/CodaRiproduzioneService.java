package com.example.business;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.domain.Brano; 
import com.example.domain.CodaRiproduzione;
import com.example.domain.ModalitaRepeat;
public interface CodaRiproduzioneService{
 void aggiungiBranoCoda(CodaRiproduzione coda, Brano brano) throws BusinessException;
 void svuotaCoda(CodaRiproduzione coda) throws BusinessException;
 void play(CodaRiproduzione coda) throws BusinessException; 
 void pausa(CodaRiproduzione coda) throws BusinessException; 
 void stop(CodaRiproduzione coda) throws BusinessException; 
 void avanti(CodaRiproduzione coda) throws BusinessException;
 void indietro(CodaRiproduzione coda) throws BusinessException; 
 void shuffle(CodaRiproduzione coda) throws BusinessException; 
 void ripristinaOrdine(CodaRiproduzione coda, List<Brano> ordineOriginale) throws BusinessException;
 void impostaRepeatCoda(CodaRiproduzione coda, ModalitaRepeat modalita) throws BusinessException;
 void branoTerminato(CodaRiproduzione coda) throws BusinessException; 
 Brano getBranoCorrente(CodaRiproduzione coda) throws BusinessException;
 List<Brano> getCoda(CodaRiproduzione coda) throws BusinessException; 
}
