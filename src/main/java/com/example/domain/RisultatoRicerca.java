package com.example.domain;
public class RisultatoRicerca{
  private final Brano brano;
  private final Album album;
  public RisultatoRicerca(Brano brano){
    this.brano=brano
    this.album=null;
  }
  public RisultatoRicerca(Album album){
    this.brano=null;
    this.album=album;
  }
  public String getTipo(){
    return brano!=null?"Brano":"Album";
  }
  public String getTitolo(){
    return brano != null ? brano.getDurata() + "sec":"-";
  }
   public String getGenere() {
        return brano != null ? brano.getGenereBrano().toString(): album.getGenere().toString();
    }
    public Brano getBrano() {
        return brano;
    }
    public Album getAlbum() {
        return album;
    }
    public int getAnnoPubblicazione(){
      if(brano != null && brano.getAlbumBrano() != null){
        return brano.getAlbumBrano().getAnnoPubblicazione();
      }
      return album != null ? album.getAnnoPubblicazione(): 0;
    }
  public int getNumeroAscolti(){
    return brano != null ? brano.getNumeroAscolti() :0;
  }
}
