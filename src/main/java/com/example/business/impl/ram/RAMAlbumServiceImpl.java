package com.example.business.impl.ram;
import java.util.ArrayList;
import java.util.List;
import com.example.business.AlbumService;
import com.example.business.BusinessException;
import com.example.business.AlbumNonTrovatoException;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Brano;

public class RAMAlbumServiceImpl implements AlbumService{
  private List<Album> album;
  public RAMAlbumServiceImpl(){
    this.album=new ArrayList<>();
  }
  @Override
  public Album findAllAlbum(String titolo) throws BusinessException{
    return new ArrayList<>(this.album);
  }
  @Override
  public Album findAlbumByTitolo(String titolo) throws BusinessException{
    if(titolo==null||titolo.trim().isEmpty()){
      throw new OperazioneNonValidaException("il titolo dell'album non è valido");
    }
    for(Album a: this.album){
      if(a.getTitolo().equalsIgnoreCase(titolo.trim())){
        return a;
      }
    }
    throw new AlbumNonTrovatoException("Album non trovato:" + titolo);
  }
  @Override
  public void creaAlbum(Album album) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non può essere nullo");
    }
    if(album.getTitolo()==null||album.getTitolo().trim().isEmpty()){
      throw new OperazioneNonValidaException("il titolo dell'album non può essere vuoto");
    }
    for(Album a: this.album){
      if(a.getTitolo().equalsIgnoreCase(album.getTitolo().trim())){
        throw new DuplicateException("esiste gia un album con il titolo:" + album.getTitolo());
      }
    }
    this.album.add(album);
  }
  @Override
  public void modificaAlbum(Album album) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non puo essere null");
    }
    if(album.getTitolo()==null||album.getTitolo().trim().isEmpty()){
      throw new OperazioneNonValidaException("il titolo dell'album non può essere null");
    }
    bulean trovato=false;
    for(int i=o; i<this.album.size(); i++){
      Album a=this.album.get(i);
      if(a==album){
        this.album.set(i, album);
        trovato=true;
        break;
      }
    }
    if(!trovato){
      throw new AlbumNonTrovatoException("Album da modificare non trovato");
    }
  }
  @Override
  public void eliminaAlbum(Album album) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non può essere null");
    }
    if (this.album.remove(album)){
      throw new AlbumNonTrovatoException("Album da eliminare non trovato");
    }
  }
  @Override
  public void aggiungiBrano(Album album, Brano brano) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non può essere null");
    }
    if(brano==null){
      trow new OperazioneNonValidaException("il brano non può essere null");
    }
    if(this.album.contains(album)){
      throw new AlbumNonTrovatoException("Album non trovato");
    }
    if(album.getList_brani().contains(brano)){
      throw new DuplicatoException("il brano è già presente nell'album");
    }
    album.getLista_brani().add(brano);
  }
  @Override
  public void rimuoviBrano(Album album, Brano brano) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("il brano non può essere null");
    }
    if(this.album.contains(album)){
      throw new AlbumNonTrovatoException("Album non trovato");
    }
    if(!album.getLista_brani().remove(brano)){
      throw new OperazioneNonValidaException("Il brano non è presente nell'album");
    }
  }
  @Override
  public void riordinaBrani(Album album, int indiceVecchio, int indiceNuovo) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non può essere null");
    }
    if(this.album.contains(album)){
      throw new OperazioneNonValidaException("L'album non può essere null");
    }
    if(this.album.contains(album)){
      throw new AlbumNonTrovatoException("Album non trovato");
    }
    List<Brano> brani=album.getLista_brani();
    if(indiceVecchio>=brani.size()){
      throw new OperazioneNonValidaException("indice vecchio non valido");
    }
    if(indiceNuovo<0||indiceNuovo>=brani.size()){
      throw new OperazioneNonValidaException("indicenuovo non valido");
    }
    Brano brano=brani.remove(indiceNuovo, brano);
    album.setList_brani(brani);
  }
  @Override
  publi List<Brano> findBrani(Album album) throws BusinessException{
    if(album==null){
      throw new OperazioneNonValidaException("L'album non può essere null");
    }
    if(this.album.contains(album)){
      throw new AlbumNonTrovatoException("Album non trovato");
    }
    return new ArrayList<>
      (album.getLista_brani());
  }
  @Override
  public int calcolaDurataTotale(Album album) throws BusinessException{
    if(album==null){
    throw new OperazioneNonValidaException("L'album non può essere null");
  }
  if(this.album.contains(album)){
      throw new AlbumNonTrovatoException("Album non trovato");
  }
  int durataTotale=0;
  for(Brano brano: album.getList_brani()){
    durataTotale+=brano.getDurata()
  }
  return durataTotale;
}
}
