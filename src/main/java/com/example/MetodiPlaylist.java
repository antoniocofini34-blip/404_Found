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
  public void aggiungiBrano(Brano brano){
    Objects.requireNonNull(brano);
    return lista_brani.add(brano);
    if(indiceCorrente==-1){
      indiceCorrente=0
    }
  }
  public boolean rimuoviBrano(Brano brano){
    Objects.requireNonNull(brano, "brano non può essere null");
    int pos=lista_brani.indiceOf(brano),
    if(pos==-1) return false;
    lista_brani.remove(pos);
    if(indiceCorrente>=lista_brani.size()){
      indiceCorrente=listaBrani.isEmpty()?-1:listaBrani.size()-1;
    }
    return true;
  }
  public void spostaBrano (int indiceVecchio, int indiceNuovo){
    checkIndice(indiceVecchio);
    if (indiceNuovo<o|| indiceNuovo>=lista_brani.size()){
      trow new IndiceException("indiceNuovo fuori range:" + indiceNuovo);
    }
    if(indiceVecchio==indiceNuovo) return;
    Brano brano=lista_brani.remove(indiceVecchio);
    lista_brani.add(indiceNuovo, brano);
    if(indiceVecchio==indiceNuovo){
      indiceVecchio=indiceNuovo;
    } else if(indiceCorrente>indiceVecchio && indiceCorrente<=indiceNuovo){
      indiceCorrente--;
    } else if(indiceCorrente<indiceVecchio && indiceCorrente>=indiceNuovo){
      indiceCorrente++;
    }
  }
  private void checkIndice(int indice){
  if(indice<0||indice>=lista_brani.size()){
    trow new IndiceException("Indice fuori range:" + indice);
  }
  }
  public int calcolaDurataTotale(){
    int totale=0;
    for(Brano b:lista_brani){
      int durata=b.getDurata();
      if(durata>o){
        totale += durata;
      }
    }
    return totale;
  }
  public void modificaDescrizione(String nuovaDescrizione){
    this.descrizione=nuovaDescrizione==null?"":nuovaDescrizione;
  }
  public void modificaNome(String nuovoNome){
    this.nome=(nuovoNome==null||nuovoNome.trim().isEmpty()?"":nuovoNome);
    //trim(). lo si usa per eliminare gli spazi
    //isEmpty() verifica se la stringa è vuota e in quel caso restituisce ""
    //? ha valore vero o falso, nel caso in cui è vera restituisce "", altrimenti restituisce il valore originale nuovoNome (vale la stessa cosa in nuovaDescrizione)
  }
}
