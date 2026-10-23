package com.example.business.impl.ram;
import java.util.ArrayList;
import java.util.List;
import com.example.business.exception.BusinessException;
import com.example.business.PlaylistService;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.business.exception.PlaylistVuotaException;
import com.example.domain.Brano;
import com.example.domain.Playlist;
public class RAMPlaylistServiceImpl implements PlaylistService { private List<Playlist> playlist;
  public RAMPlaylistServiceImpl(){ this.playlist=new ArrayList<>(); }
  @Override
  public void creaPlaylist(Playlist playlist) throws BusinessException {
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
    if(playlist.getNome()==null||playlist.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("il nome della playlist non può essere vuoto");
    for(Playlist p: this.playlist) {
      if(p.getNome().equalsIgnoreCase(playlist.getNome().trim())) throw new DuplicatoException("Errore: esiste già una playlist con il nome:" + playlist.getNome());
    }
    this.playlist.add(playlist);
  }
  @Override
  public void modificaPlaylist(String nomeVecchio, Playlist playlist) throws BusinessException {
    if (nomeVecchio==null) throw new OperazioneNonValidaException("Il vecchio nome della playlist non può essere vuoto");
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
    if(playlist.getNome()==null || playlist.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto");
    boolean trovata=false;
    for(int i=0; i<this.playlist.size(); i++) {
      Playlist playlistEsistente=this.playlist.get(i);
      if(playlistEsistente.getNome().equalsIgnoreCase(nomeVecchio)) { this.playlist.set(i, playlist);
        trovata=true;
        break;
      }
    }
    if(!trovata) throw new OperazioneNonValidaException("Errore: playlist non trovata");
  }
  @Override
  public void eliminaPlaylist(Playlist playlist) throws BusinessException {
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
    if (!this.playlist.remove(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
  }
  @Override
  public List<Playlist> findAllPlaylist() throws BusinessException { return new ArrayList<>(this.playlist); }
  @Override
  public Playlist findPlaylistByNome(String nome) throws BusinessException {
      if (nome == null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto");
      for(Playlist playlist : this.playlist) {
        if(playlist.getNome().equalsIgnoreCase(nome.trim())) return playlist;
      }
    throw new OperazioneNonValidaException("Errore: playlist non trovata:" + nome);
  }
  @Override
  public void aggiungiBrano(Playlist playlist, Brano brano) throws BusinessException {
        if (playlist == null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
        if (brano == null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
        if (!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
        List<Brano> brani = playlist.getListaBrani();
        if (brani.contains(brano)) throw new DuplicatoException("Errore: brano già presente nella playlist");
        brani.add(brano);
        playlist.setListaBrani(brani);
    }
  @Override
  public void rimuoviBrano(Playlist playlist, Brano brano) throws BusinessException {
        if (playlist == null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
        if (brano == null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
        if (!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
        List<Brano> brani = playlist.getListaBrani();
        if (!brani.remove(brano)) throw new OperazioneNonValidaException("Errore: brano non presente nella playlist");
        playlist.setListaBrani(brani);
        if (playlist.getIndiceCorrente() >= brani.size()) playlist.setIndiceCorrente(-1);
    }
  @Override
   public void riordinaBrani(Playlist playlist, int indiceVecchio, int indiceNuovo) throws BusinessException {
        if (playlist == null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
        if (!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
        List<Brano> brani = playlist.getListaBrani();
        if (brani.isEmpty()) throw new PlaylistVuotaException("Errore: non è possibile riordinare una playlist vuota");
        if (indiceVecchio < 0|| indiceVecchio >= brani.size()) throw new OperazioneNonValidaException("Indice vecchio non valido: " + indiceVecchio);
        if (indiceNuovo < 0|| indiceNuovo >= brani.size()) throw new OperazioneNonValidaException("Indice nuovo non valido: " + indiceNuovo);
       Brano brano=brani.remove(indiceVecchio);
       brani.add(indiceNuovo, brano);
       playlist.setListaBrani(brani);
       playlist.setIndiceVecchio(indiceVecchio);
       playlist.setIndiceNuovo(indiceNuovo);
     }
  @Override
  public List<Brano> getBrani(Playlist playlist) throws BusinessException {
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
    if(!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
    return new ArrayList<>(playlist.getListaBrani());
  }
  @Override
  public int calcolaDurataTotale(Playlist playlist) throws BusinessException {
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
    if(!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
    int durataTotale=0;
    for(Brano brano : playlist.getListaBrani()) {
      if(brano!=null) durataTotale+=brano.getDurata();
    }
    return durataTotale;
  }
  @Override
  public void riproduciPlaylist(Playlist playlist) throws BusinessException {
      if (playlist == null) throw new OperazioneNonValidaException("La playlist non può essere vuota");
      if (!this.playlist.contains(playlist)) throw new OperazioneNonValidaException("Errore: playlist non trovata");
      if (playlist.getListaBrani().isEmpty()) throw new PlaylistVuotaException("Errore: impossibile riprodurre una playlist vuota");
      playlist.setIndiceCorrente(0);
  }
}
