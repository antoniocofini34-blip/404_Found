package com.example.business.impl.file;
import com.example.business.AlbumService;
import com.example.business.ArtistaService;
import com.example.business.BranoService;
import com.example.business.CatalogoService;
import com.example.business.CodaRiproduzioneService;
import com.example.business.CronologiaAscoltiService;
import com.example.business.PlaylistService;
import com.example.business.RicercaService;
import com.example.business.StatisticheService;
import com.example.business.UtenteService;
import com.example.business.UnivaqSoundBusinessFactory;
import com.example.business.impl.ram.RAMCatalogoServiceImpl;
import com.example.business.impl.ram.RAMCodaRiproduzioneServiceImpl;
import com.example.business.impl.ram.RAMCronologiaAscoltiServiceImpl;
import com.example.business.impl.ram.RAMRicercaServiceImpl;
import com.example.business.impl.ram.RAMStatisticheServiceImpl;
public class FileUnivaqSoundBusinessFactoryImpl() implements UnivaqSoundBusinessFactory {
  private FileArtistaServiceImpl artistaService;
  private FileAlbumServiceImpl albumService;
  private FileBranoServiceImpl branoService;
  private RAMCatalogoServiceImpl catalogoService;
  private RAMCodaRiproduzioneServiceImpl codaRiproduzioneService;
  private RAMCronologiaAscoltiServiceImpl cronologiaAscoltiService;
  private FilePlaylistServiceImpl playlistService;
  private RAMRicercaServiceImpl ricercaService;
  private RAMStatisticheServiceImpl statisticheService;
  private FileUtenteServiceImpl utenteService;
  public FileUnivaqSoundBusinessFactoryImpl() {
    this.artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    this.branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    this.albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    this.playlistService=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt");
    this.utenteService=new FileUtenteServiceImpl("src/main/resources/dati/utenti.txt");
    this.catalogoService=new RAMCatalogoServiceImpl();
    this.codaRiproduzioneService=new RAMCodaRiproduzioneServiceImpl();
    this.cronologiaAscoltiService=new RAMCronologiaAscoltiServiceImpl();
    this.ricercaService=new RAMRicercaServiceImpl(this.branoService, this.albumService, this.artistaService);
    this.statisticheService=new RAMStatisticheServiceImpl(this.branoService, this.artistaService, this.cronologiaAscoltiService);
  }
  @Override
  public ArtistaService getArtistaService() { return this.artistaService; }
  @Override
  public AlbumService getAlbumService() { return this.albumService; }
  @Override
  public BranoService getBranoService() { return this.branoService; }
  @Override
  public CatalogoService getCatalogoService() { return this.catalogoService; }
  @Override
  public CodaRiproduzioneService getCodaRiproduzioneService() { return this.codaRiproduzioneService; }
  @Override
  public CronologiaAscoltiService getCronologiaAscoltiService() { return this.cronologiaAscoltiService; }
  @Override
  public PlaylistService getPlaylistService() { return this.playlistService; }
  @Override
  public RicercaService getRicercaService() { return this.ricercaService; }
  @Override
  public StatisticheService getStatisticheService() { return this.statisticheService; }
  @Override
  public UtenteService getUtenteService() { return this.utenteService; }
}
