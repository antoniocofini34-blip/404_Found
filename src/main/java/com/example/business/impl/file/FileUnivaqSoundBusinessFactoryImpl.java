package com.example.business.impl.file;
import com.example.business.AlbumService;
import com.example.business.ArtistaService;
import com.example.business.BranoService;
import com.example.business.UnivaqSoundBusinessFactory;
public class FileUnivaqSoundBusinessFactoryImpl implements UnivaqSoundBusinessFactory {
  private FileArtistaServiceImpl artistaService;
  private FileAlbumServiceImpl albumService;
  private FileBranoServiceImpl branoService;
  public FileUnivaqSoundBusinessFactoryImpl() {
    this.artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    this.branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    this.albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
  }
  @Override
  public ArtistaService getArtistaService() { return this.artistaService; }
  @Override
  public AlbumService getAlbumService() { return this.albumService; }
  @Override
  public BranoService getBranoService() { return this.branoService; }
}
