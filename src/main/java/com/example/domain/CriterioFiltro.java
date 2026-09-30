package com.example.domain;
@FunctionalInterface
  public interface CriterioFiltro<T>{
    boolean verifica(T elemento);
  }
