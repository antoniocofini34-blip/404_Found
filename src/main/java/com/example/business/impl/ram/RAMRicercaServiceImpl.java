package com.example.business.impl.ram;
import java.util.*;
import com.example.business.BusinessException;
import com.example.business.RicercaService;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.Brano;
import com.example.domain.CriterioFiltro;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
public class RAMRicercaServiceImpl implements RicercaService {
  private RAMBranoServiceImpl branoService;
  private RAMAlbumServiceImpl albumService;
  private RAMArtistaServiceImpl artistaService;
  public RAMRicercaServiceImpl (RAMBranoServiceImpl branoService, RAMAlbumServiceImpl albumService, RAMArtistaServiceImpl artistaService) {
    if (branoService==null) throw new IllegalArgumentException("Il servizio dei brani non può essere null");
    if (albumService==null) throw new IllegalArgumentException("Il servizio degli album non può essere null");
    if (artistaService==null) throw new IllegalArgumentException("Il servizio degli artisti non può essere null");
  }
  @Override
  public List<Brano> cercaBrani(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty()) throw new OperazioneNonValidaException("Il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Brano> risultati=new ArrayList<>();
    for (Brano brano : branoService.findAllBrani()) {
      if(brano.getTitolo()!=null && brano.getTitolo().toLowerCase().contains(testoRicerca))
        risultati.add(brano);
    }
    return risultati;
  }
  @Override
  public List<Album> cercaAlbum(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty())
      throw new OperazioneNonValidaException("Il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Album> risultati=new ArrayList<>();
    for (Album album: albumService.findAllAlbum()) {
      if(album.getTitolo()!=null && album.getTitolo().toLowerCase().contains(testoRicerca))
        risultati.add(album);
    }
    return risultati;
  }
  @Override
  public List<Artista> cercaArtisti(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty())
      throw new OperazioneNonValidaException("Il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Artista> risultati=new ArrayList<>();
    for (Artista artista: artistaService.findAllArtisti()) {
      String nomeArtista=ottieniNomeArtista(artista);
      if (nomeArtista!=null && nomeArtista.toLowerCase().contains(testoRicerca)) 
        risultati.add(artista);
    }
    return risultati;
  }
  @Override
  public <T> List<T> filtra(List<T> elementi, CriterioFiltro<T> criterio) throws BusinessException {
    if (elementi==null) throw new OperazioneNonValidaException("La lista degli elemtni non può essere null");
    if (criterio==null) throw new OperazioneNonValidaException("Il criterio di filtro non può essere null");
    List<T> risultati= new ArrayList<>();
    for (T elemento: elementi) {
      if (elemento!=null && criterio.verifica(elemento)) risultati.add(elemento);
    }
    return risultati;
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) {
      ArtistaSolista solista=(ArtistaSolista) artista;
      return solista.getNomeArte();
    } else if (artista instanceof ArtistaGruppo) {
      ArtistaGruppo gruppo=(ArtistaGruppo) artista;
      return gruppo.getNomeGruppo();
    }
    return null;
  }
}
  





      
      
