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

public class RAMPlaylistService{
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
  @Override
  @Override
  @Override
  @Override
}
