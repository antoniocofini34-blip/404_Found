package com.example.domain.Catalogo;
import java.util.*;
import com.example.domain.Catalogo;
public interface CatalogoService{
  <T>void aggiungiElemento(Catalogo<T> catalogo, T elemento) throws BusinessException;
  <T>void rimuoviElemento(Catalogo<T> catalogo, T elemento) throws BusinessException;
  <T>List<T> getElementi(Catalogo<T> catalogo) throws BusinessException;
  <T>void svuotaCatalogo(Catalogo<T> catalogo) throws BusinessException;
  <T>void contaElementi(Catalogo<T> catalogo) throws BusinessException;
}
