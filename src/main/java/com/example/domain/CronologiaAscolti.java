package com.example.domain;
import java.util.*; 

public class CronologiaAscolti {
  private List<Ascolto> registro_ascolti; 
  public CronologiaAscolti (List<Ascolto> registro_ascolti) {
    if (registro_ascolti!=null) this.registro_ascolti=new ArrayList<>(registro_ascolti);
    else this.registro_ascolti=new ArrayList<>(); 
  }
  public List<Ascolto> getRegistroAscolti() {
    return new ArrayList<>(this.registro_ascolti); 
  }   
  public void setRegistroAscolti(List<Ascolto> registro_ascolti) {
    if (registro_ascolti!=null) this.registro_ascolti=new ArrayList<>(registro_ascolti); 
    else this.registro_ascolti=new ArrayList<>(); 
  }
}
    

  
    
    
    
  
  



