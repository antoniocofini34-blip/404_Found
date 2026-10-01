package com.example.business.impl.ram;
import java.util.ArrayList;
import java.util.List;
import com.example.business.BusinessException;
import com.example.business.PlaylistService;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.business.exception.PlaylistVuotaException;
import com.example.domain.Brano;
import com.example.domain.Playlist;

public class RAMPlaylistServiceImpl implements PlaylistService{
  private List<Playlist> playlist;
  public RAMPlaylistServiceImpl(){
    this.playlist=new ArrayList<>();
  }
  @Override
  public void creaPlaylist(Playlist playlist) throws BusinessException{
    if(playlist==null){
      throw new OperazioneNonValidaException("La playlist non può essere null");
    }
    if(playlist.getNome()==null||playlist.getNome().trim.isEmpty()){
      throw new OperazioneNonValidaException("il nome della playlist non può essere vuoto");
    }
    for(Playlist p: this.playlist){
      if(p.getNome().equalsIgnoreCase(playlist.getNome().trim())){
        throw new DuplicatoExceptiom("Esiste già una playlist con il nome:" + playlist.getNome());
      }
    }
    this.playlist.add(playlist);
  }
  @Override
  public void modificaPlaylist(Playlist playlist) throws BusinessException{
    if(playlist==null){
      throw new OperazioneNonValidaException("La playlist non può essere null");
    }
    if(playlist.getNome()==null || playlist.getNome().trim().isEmpty()){
      throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto");
    }
    boolean trovata=false;
    for(int i=0; i<this.playlist.size(); i++){
      Playlist.playlistEsistente=this.playlist.get(i);
      if(playlistEsistente==playlist){
        this.playlist.set(i, playlist);
        trovata=true;
        break;
      }
    }
    if(!trovata){
      throw new OperazioneNonValidaException("La playlistt da modificare non è presente");
    }
  }
  @Override
  public void eliminaPlaylist(Playlist playlist) throwe BusinessException{
    if(playlist==null){
      throw new OperazioneNonValidaException("La playlist non può essere null")
    }
    if (!this.playlist.remove(playlist)) {
      throw new OperazioneNonValidaException("La playlist da eliminare non è presente");
    }
  }
  @Override
  public List<Playlist> findAllPlaylist() throws BusinessException {
      return new ArrayList<>(this.playlist);
   }
  @Override
  public Playlist findPlaylistByNome(String nome) throws BusinessException {
      if (nome == null || nome.trim().isEmpty()) {
            throw new OperazioneNonValidaException("Il nome della playlist non è valido");
      }
      for(Playlist playlist: this.playlist){
        if(playlist.getNome().equalsIgnoreCase(nome.trim())){
          return playlist;
        }
      }
    throw new OperazioneNonValidaException("Playlist non trovata:" + nome);
  }
  @Override
  public void aggiungiBrano(Playlist playlist, Brano brano) throws BusinessException {
        if (playlist == null) {
          throw new OperazioneNonValidaException("La playlist non può essere null");
        }
        if (brano == null) {
          throw new OperazioneNonValidaException("Il brano non può essere null");
        }
        if (!this.playlist.contains(playlist)) {
          throw new OperazioneNonValidaException("La playlist non è presente");
        }
         List<Brano> brani = playlist.getLista_brani();
        if (brani.contains(brano)) {
            throw new DuplicatoException("Il brano è già presente nella playlist");
        }
        brani.add(brano);
        playlist.setLista_brani(brani);
    }
  @Override
  public void rimuoviBrano(Playlist playlist, Brano brano) throws BusinessException {
        if (playlist == null) {
            throw new OperazioneNonValidaException("La playlist non può essere null");
        }
        if (brano == null) {
            throw new OperazioneNonValidaException("Il brano non può essere null");
        }
        if (!this.playlist.contains(playlist)) {
            throw new OperazioneNonValidaException("La playlist non è presente");
        }
         List<Brano> brani = playlist.getLista_brani();
        if (!brani.remove(brano)) {
            throw new OperazioneNonValidaException("Il brano non è presente nella playlist");
        }
        playlist.setLista_brani(brani);
        if (playlist.getIndiceCorrente() >= brani.size()) {
            playlist.setIndiceCorrente(-1);
        }
    }
  @Override
   public void riordinaBrani(Playlist playlist, int indiceVecchio, int indiceNuovo) throws BusinessException {
        if (playlist == null) {
            throw new OperazioneNonValidaException("La playlist non può essere null");
        }
        if (!this.playlist.contains(playlist)) {
            throw new OperazioneNonValidaException("La playlist non è presente");
        }
        List<Brano> brani = playlist.getLista_brani();
        if (brani.isEmpty()) {
            throw new PlaylistVuotaException("Non è possibile riordinare una playlist vuota");
        }
        if (indiceVecchio < 0|| indiceVecchio >= brani.size()) {
            throw new OperazioneNonValidaException("Indice vecchio non valido: " + indiceVecchio);
        }
        if (indiceNuovo < 0|| indiceNuovo >= brani.size()) {
            throw new OperazioneNonValidaException("Indice nuovo non valido: " + indiceNuovo);
        }
     Brano brano=brani.remove(indiceVecchio);
     brani.add(indiceNuovo, brano);
     playlist.setList_brani(brani);
     playlist.setIndiceVecchio(indiceVecchio);
     playlist setIndiceNuovo(indiceNuovo);
   }
  @Override
  public List<Brano> getBrani(Playlist playlist) throws BusinessExceptions{
    if(playlist==null){
      throw new OperazioneNonValidaException("La playlist non può essere null");
    }
    if(!this.playlist.contains(playlist)){
      throw new OperazioneNonValidaException("La playlist non è presente");
    }
    return new ArrayList<>(playlist.getLista_brani);
  }
  @Override
  public int calcolaDurataTotale(Playlist playlist) throws BusinessException{
    if(playlist==null){
      throw new OperazioneNonValidaException("La playlist non può essere null");
    }
    if(!this.playlist.contains(playlist)){
      throw new OperazioneNonValidaException("La playlist non è presente");
    }
    int durataTotale=0;
    for(Brano brano: playlist.getLista_brani()){
      if(brano!=null){
        durataTotale+=brano.getDurata();
      }
    }
    return durataTotale;
  }
  @Override
  public void riproduciPlaylist(Playlist playlist) throws BusinessException {
      if (playlist == null) {
          throw new OperazioneNonValidaException("La playlist non può essere null");
      }
      if (!this.playlist.contains(playlist)) {
          throw new OperazioneNonValidaException("La playlist non è presente");
      }
      if (playlist.getLista_brani().isEmpty()) {
          throw new PlaylistVuotaException("Non è possibile riprodurre una playlist vuota");
      }
      playlist.setIndiceCorrente(0);
  }
}
