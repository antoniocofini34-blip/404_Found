package com.example.business.impl.ram;
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
public class RAMUnivaqSoundBusinessFactoryImpl implements UnivaqSoundBusinessFactory {
  private RAMArtistaServiceImpl artistaService;
  private RAMAlbumServiceImpl albumService;
  private RAMBranoServiceImpl branoService;
  private RAMCatalogoServiceImpl catalogoService;
  private RAMCodaRiproduzioneServiceImpl codaRiproduzioneService;
  private RAMCronologiaAscoltiServiceImpl cronologiaAscoltiService;
  private RAMPlaylistServiceImpl playlistService;
  private RAMRicercaServiceImpl ricercaService;
  private RAMStatisticheServiceImpl statisticheService;
  private RAMUtenteServiceImpl utenteService;
  public RAMUnivaqSoundBusinessFactoryImpl() {
    this.artistaService=new RAMArtistaServiceImpl();
    this.albumService=new RAMAlbumServiceImpl();
    this.branoService=new RAMBranoServiceImpl();
    this.catalogoService=new RAMCatalogoServiceImpl();
    this.codaRiproduzioneService=new RAMCodaRiproduzioneServiceImpl();
    this.cronologiaAscoltiService=new RAMCronologiaAscoltiServiceImpl();
    this.playlistService=new RAMPlaylistServiceImpl();
    this.utenteService=new RAMUtenteServiceImpl();
    this.ricercaService=new RAMStatisticheServiceImpl(this.branoService, this.albumService, this.artistaService);
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
