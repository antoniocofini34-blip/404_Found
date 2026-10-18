package com.example.business.impl.ram; 
import java.util.*; 
import com.example.business.exception.BusinessException; 
import com.example.business.CatalogoService; 
import com.example.business.exception.DuplicatoException; 
import com.example.business.exception.OperazioneNonValidaException; 
import com.example.domain.Catalogo; 
public class RAMCatalogoServiceImpl implements CatalogoService {
  @Override 
  public <T> void aggiungiElemento(Catalogo<T> catalogo, T elemento) throws BusinessException {
    if (catalogo==null) throw new OperazioneNonValidaException("Il catalogo non può essere vuoto"); 
    if (elemento==null) throw new OperazioneNonValidaException("L'elemento non può essere vuoto"); 
    List<T> elementi=catalogo.getElementi(); 
    if (elementi.contains(elemento)) throw new DuplicatoException("L'elemento esiste già nel catalogo");
    elementi.add(elemento); 
    catalogo.setElementi(elementi); 
  }
  @Override 
  public <T> void rimuoviElemento(Catalogo<T> catalogo, T elemento) throws BusinessException {
    if (catalogo==null) throw new OperazioneNonValidaException("Il catalogo non può essere vuoto"); 
    if (elemento==null) throw new OperazioneNonValidaException("L'elemento non può essere vuoto");
    List<T> elementi=catalogo.getElementi(); 
    if (!elementi.remove(elemento)) throw new OperazioneNonValidaException("L'elemento non esiste nel catalogo");
    catalogo.setElementi(elementi); 
  }
  @Override 
  public <T> List<T> getElementi(Catalogo<T> catalogo) throws BusinessException {
    if (catalogo==null) throw new OperazioneNonValidaException("Il catalogo non può essere vuoto");
    return new ArrayList<>(catalogo.getElementi()); 
  }
  @Override 
  public <T> void svuotaCatalogo(Catalogo<T> catalogo) throws BusinessException {
    if (catalogo==null) throw new OperazioneNonValidaException("Il catalogo non può essere vuoto");
    catalogo.setElementi(new ArrayList<>()); 
  }
  @Override
  public <T> int contaElementi(Catalogo<T> catalogo) throws BusinessException {
    if (catalogo==null) throw new OperazioneNonValidaException("Il catalogo non può essere vuoto"); 
    return catalogo.getElementi().size(); 
  }
} 
