import java.util.*; 
public class CronologiaAscolti {
  private List<Ascolto> registro_Ascolti; 
  public Cronologia_Ascolti (List<Ascolto> registro_Ascolti) {
    this.registro_Ascolti=new ArrayList<>(); 
  }
  public List<Ascolto> getRegistroAscolti() {
    return registro_Ascolti;
  }
  public void aggiungiAscolto(Ascolto ascolto) {
    if (ascolto!=null) this.registro_Ascolti.add(ascolto); 
  }
  public void rimuoviAscolto(Ascolto ascolto) {
    this.registro_Ascolti.remove(ascolto);
  }
  public void svuotaCronologia() {
    this.registro_Ascolti.clear(); 
  }
  public List<Ascolto> getUltimiAscolti() {
    return new ArrayList<>(this.registro_Ascolti);
  }
  public int calcolaTempoTotaleAscolto() {
    int tempoTotale=0; 
    for (Ascolto ascolto : registro_Ascolti) {
      tempoTotale += ascolto.getTempoBranoAscoltato();
    }
    return tempoTotale; 
  }
  public int contaAscolti(Brano brano) {
    if (brano==null return 0; 
    int conteggio=0; 
    for (Ascolto ascolto : registro_Ascolti) {
      if (brano.equals(ascolto.getBranoAscoltato())) {
        conteggio ++; 
      }
    }
    return conteggio; 
  }
  public List<Brano> ottieniBraniPiuAscoltati() {
    Map<Brano, Integer> mappaConteggio=new HashMap<>(); 
    for (Ascolto ascolto : registro_Ascolti) {
      Brano brano=ascolto.getBranoAscoltato(); 
      if (brano!=null) {
        mappaConteggio.put(brano, mappaConteggio.getOrDefault(brano, 0) + 1);
      }
    }
    List<Brano> braniPiuAscoltati=new ArrayList<>(mappaConteggio.keySet()); 
    return braniPiuAscoltati; 
  }
  public List<Artista> ottieniArtistiPiuAscoltati() {
    Map<Artista, Integer> mappaConteggio=new HashMap<>(); 
    for (Ascolto ascolto : registro_Ascolti) {
      Brano brano=ascolto.getBranoAscoltato(); 
      if (brano!=null) {
        Artista artista=brano.getAlbumBrano().getArtista(); 
        if (artista!=null) {
          mappaConteggio.put(artista, mappaConteggio.getOrDefault(artista, 0) + 1); 
        }
      }
    }
    List<Artista> artistiPiuAscoltati=new ArrayList<>(mappaConteggio.keySet());
    return artistiPiuAscoltati;    
  }
    

  
    
    
    
  
  



