import java.util.*; 
package com.example.domain;

public class CronologiaAscolti {
  private List<Ascolto> registro_Ascolti; 
  public CronologiaAscolti (List<Ascolto> registro_Ascolti) {
    this.registro_Ascolti=new ArrayList<>(); 
  }
  public List<Ascolto> getRegistroAscolti() {
    return new ArrayList<>(this.registro_Ascolti); 
  }   
  public void setRegistroAscolti(List<Ascolto> registro_Ascolti) {
    if (registro_Ascolti!=null) this.registro_Ascolti=new ArrayList<>(registro_Ascolti); 
    else this.registro_Ascolti=new ArrayList<>(); 
  }
}
    

  
    
    
    
  
  



