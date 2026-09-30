public class Playlist{
  private List<Brano> lista_brani;
  private String nome;
  private String descrizione;
  private localDate data_creazione;
  public Playlist(String nome, String descrizione, localDate data_creazione){
    this.lista_brani=new ArrayList<>();
    this.nome=nome;
    this.descrizione=descrizione;
    this.data_creazione=data_creazione;
    this.indiceCorrente=-1
  }
  public List<Brano> getLista_brani(){
    return lista_brani;
  }
  public String getNome(){
    return nome;
  }
  public String getDescrizione(){
    return descrizione;
  }
  public localDate getData_creazione(){
    return data_creazione;
  }
  public void setNome(String nome){
    this.nome=nome;
  }
  public void setDescrizione(String descrizione){
    this.descrizione=descrizione;
  }
  public void setData_creazione(String data_creazione){
    this.data_creazione=data_creazione;
  }
  public void setLista_brani(List<Brano> nuovaLista_brani){
    if(nuovaLista_brani==null){
      this.lista_brani=new ArrayList<>()
    } else{
      this.lista_brani=new ArrayList<>(nuovaLista_brani)
    }
    this.indiceCorrente=-1
  }
  public int getIndiceCorrente(){
    return indiceCorrente;
  }
  public void setIndiceCorrente(int indiceCorrente){
    if(indiceCorrente<-1||indiceCorrente>=lista_brani.size()){
      trow new indiceVecchio("Indice corrente." + indiceCorrente);
    }
  }
}
