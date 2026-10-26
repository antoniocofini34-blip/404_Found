package com.example.business.impl.ram;
import com.example.business.AlbumService;
import com.example.business.ArtistaService;
import com.example.business.BranoService;
import com.example.business.UnivaqSoundBusinessFactory;
public class RAMUnivaqSoundBusinessFactoryImpl implements UnivaqSoundBusinessFactory {
  private RAMArtistaServiceImpl artistaService;
  private RAMAlbumServiceImpl albumService;
  private RAMBranoServiceImpl branoService;
  public RAMUnivaqSoundBusinessFactoryImpl() {
    this.artistaService=new RAMArtistaServiceImpl();
    this.albumService=new RAMAlbumServiceImpl();
    this.branoService=new RAMBranoServiceImpl();
  }
  @Override
  public ArtistaService getArtistaService() { return this.artistaService; }
  @Override
  public AlbumService getAlbumService() { return this.albumService; }
  @Override
  public BranoService getBranoService() { return this.branoService; }
}
