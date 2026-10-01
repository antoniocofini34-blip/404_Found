package com.example.domain; 
import java.util.*; 

public class Catalogo<T> {
  private List<T> elementi; 
  public Catalogo() {
    this.elementi=new ArrayList<>(); 
  }
  public Catalogo(List<T> elementi) {
    if (elementi!=null) {
      this.elementi=new ArrayList<>(elementi); 
    }
    else {
      this.elementi=new ArrayList<>(); 
    }
  }
  public List<T> getElementi() {
    return new ArrayList<>(this.elementi); 
  }
  public void setElementi(List<T> elementi) {
    if (elementi!=null) this.elementi=new ArrayList<>(elementi); 
    else this.elementi=new ArrayList<>(); 
  }
}
  
  
  
