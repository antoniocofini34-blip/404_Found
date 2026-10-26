package com.example.business.impl.ram;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.business.RicercaService;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.Brano;
import com.example.domain.CriterioFiltro;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
public class RAMRicercaServiceImpl implements RicercaService { private RAMBranoServiceImpl branoService;
  private RAMAlbumServiceImpl albumService;
  private RAMArtistaServiceImpl artistaService;
  public RAMRicercaServiceImpl (RAMBranoServiceImpl branoService, RAMAlbumServiceImpl albumService, RAMArtistaServiceImpl artistaService) {
    if (branoService==null) throw new IllegalArgumentException("Il servizio dei brani non può essere vuoto");
    if (albumService==null) throw new IllegalArgumentException("Il servizio degli album non può essere vuoto");
    if (artistaService==null) throw new IllegalArgumentException("Il servizio degli artisti non può essere vuoto");
  this.branoService=branoService;
  this.albumService=albumService;
  this.artistaService=artistaService;
}
  @Override
  public List<Brano> cercaBrani(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty()) throw new OperazioneNonValidaException("Errore: il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Brano> risultati=new ArrayList<>();
    for (Brano brano : branoService.findAllBrani()) {
      if(brano.getTitolo()!=null && brano.getTitolo().toLowerCase().contains(testoRicerca)) risultati.add(brano);
    }
    return risultati;
  }
  @Override
  public List<Album> cercaAlbum(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty()) throw new OperazioneNonValidaException("Errore: il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Album> risultati=new ArrayList<>();
    for (Album album: albumService.findAllAlbum()) {
      if(album.getTitolo()!=null && album.getTitolo().toLowerCase().contains(testoRicerca)) risultati.add(album);
    }
    return risultati;
  }
  @Override
  public List<Artista> cercaArtista(String testo) throws BusinessException {
    if (testo==null || testo.trim().isEmpty()) throw new OperazioneNonValidaException("Errore: il testo di ricerca non può essere vuoto");
    String testoRicerca=testo.trim().toLowerCase();
    List<Artista> risultati=new ArrayList<>();
    for (Artista artista : artistaService.findAllArtisti()) {
      String nomeArtista=ottieniNomeArtista(artista);
      if (nomeArtista!=null && nomeArtista.toLowerCase().contains(testoRicerca)) risultati.add(artista);
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
