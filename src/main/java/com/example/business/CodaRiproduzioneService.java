package com.example.busines;
import java.util.*;
import com.example.domain.Brano; 
import com.example.domain.CodaRiproduzione; 

public interface CodaRiproduzioneService{
 void aggiungiBranoCoda(CodaRiproduzione coda, Brano brano) throws BusinessException;
 void rimuoviBranoCoda(CodaRiproduzione coda, Brano brano) throws BusinessException;
 void svuotaCoda(CodaRiproduzione coda) throws BusinessException;
 void play(CodaRiproduzione coda) throws BusinessException; 
 void pausa(CodaRiproduzione coda) throws BusinessException; 
 void stop(CodaRiproduzione coda) throws BusinessException; 
 void avanti(CodaRiproduzione coda) throws BusinessException;
 void indietro(CodaRiproduzione coda) throws BusinessException; 
 void shuffle(CodaRiproduzione coda) throws BusinessException; 
 void impostaRepeatCoda(CodaRiproduzione coda, ModalitaRepeat modalita) throw BusinessException;
 void branoTerminato(CodaRiproduzione coda) throws BusinessException; 
}
