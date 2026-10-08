package com.example.business.impl.file; 
import java.io.File; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.*; 
import com.example.business.AlbumService; 
import com.example.business.BusinessException; 
import com.example.business.exception.AlbumNonTrovatoException;
import com.example.business.exception.DuplicatoException; 
import com.example.business.exception.OperazioneNonValidaException; 
import com.example.domain.Album; 
import com.example.domain.Artista; 
import com.example.domain.Brano; 
import com.example.domain.GenereMusicale; 
public class FileAlbumServiceImpl implements AlbumService { private String filename; 
  public FileAlbumService(String filename) { this.filename=filename; }
  @Override 
  public List<Album> findAllAlbum() throws BusinessException {
    try { FileData data=leggiDati(); 
      return convertiRighe(data); 
    }
    catch (IOException e) { throw new BusinessException("Errore durante la lettura degli album", e); }
  }
  @Override 
  public Album findAlbumByTitolo(String titolo) throws BusinessException {
    if (titolo==null || titolo.isEmpty()) { throw new OperazioneNonValidaException("Il titolo dell'album non è valido"); }
    List<Album> album=findAllAlbum(); 
    for (ALbum a : album) {
      if (a.getTitolo()!=null && a.getTitolo().equalsIgnoreCase(titolo.trim())) { return a; }
    }
    throw new AlbumNonTrovatoException("Album non trovato:" + titolo); 
  }
  @Override
  public void creaAlbum(Album album) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    if (album.getTitolo()==null || album.getTitolo().trim().isEmpty()) { throw new OperazioneNonValidaException("Il titolo dell'album non può essere vuoto"); }
    if (album.getArtista()==null) { throw new OperazioneNonValidaException("L'artista dell'album non può essere null"); }
    List<Album> albumEsistenti=findAllAlbum(); 
    for (Album a : albumEsistenti) {
      if (a.getTitolo()!=null && a.getTitolo().equalsIgnoreCase(album.getTitolo().trim())) { throw new DuplicatoException("Esiste già un album con il titolo:" + album.getTitolo()); }
    }
    albumEsistenti.add(album); 
    scriviAlbum(albumEsistenti); 
  }
  @Override 
  public void modificaAlbum(Album album) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    if (album.getTitolo()==null || album.getTitolo().trim().isEmpty()) { throw new OperazioneNonValidaException("Il titolo dell'album non può essere vuoto"); }
    List<Album> albumEsistenti=findAllAlbum(); 
    boolean trovato=false; 
    for (int i=0; i < albumEsistenti.size(); i++) {
      Album albumEsistente=albumEsistenti.get(i); 
      if (albumEsistente.getTitolo()!=null && albumEsistente.getTitolo().equalsIgnoreCase(album.getTitolo().trim())) { albumEsistenti.set(i, album); 
        trovato=true; 
        break; 
      }
    }
    if (!trovato) { throw new AlbumNonTrovatoException("Album da modificare non trovato"); 
    }
    scriviAlbum(albumEsistenti); 
  }
  @Override
  public void eliminaAlbum(Album album) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); 
    }
    if (album.getTitolo()==null || album.getTitolo().trim().isEmpty()) { throw new OperazioneNonValidaException("Il titolo dell'album non può essere vuoto"); 
    }
    List<Album> albumEsistenti=findAllAlbum(); 
    boolean rimosso=false; 
    for (int i=0; i < albumEsistenti.size(); i++) {
      Album albumEsistente=albumEsistenti.get(i); 
      if (albumEsistente.getTitolo()!=null && albumEsistente.getTitolo().equalsIgnoreCase(album.getTitolo().trim())) { albumEsistenti.remove(i); 
        rimosso=true; 
        break; 
      }
    }
    if (!rimosso) { throw new AlbumNonTrovatoException("Album da eliminare non trovato"); }
    scriviAlbum(albumEsistenti); 
  }
  @Override
  public void aggiungiBrano(Album album, Brano brano) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    if (brano==null) { throw new OperazioneNonValidaException("Il brano non può essere null"); }
    Album albumFile=findAlbumByTitolo(album.getTitolo()); 
    List<Brano> brani=albumFile.getListaBrani(); 
    for (Brano b : brani) { if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo())) { throw new DuplicatoException("Il brano è già presente nell'album"); }
    }
    brano.setAlbumBrano(albumFile); 
    brani.add(brano); 
    albumFile.setBrano(brani); 
    modificaAlbum(albumFile); 
  }
  @Override
  public void rimuoviBrano(Album album, Brano brano) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    if (brano==null) { throw new OperazioneNonValidaException("Il brano non può essere null"); }
    Album albumFile=findAlbumByTitolo(album.getTitolo()); 
    List<Brano> brani=albumFile.getListaBrani(); 
    boolean rimosso=false; 
    for (int i=0; i < brani.size(); i++) {
      Brano b=brani.get(i); 
      if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo())) { brani.remove(i); 
        rimosso=true; 
        break; 
      }
    }
    if (!rimosso) { throw new OperazioneNonValidaException("Il brano non è presente nell'album"); }
    albumFile.setBrano(brani); 
    modificaAlbum(albumFile); 
  }
  @Override 
  public void riordinaBrani(Album album, int indiceVecchio, int indiceNuovo) throw BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    Album albumFile=findAlbumByTitolo(album.getTitolo()); 
    List<Brano> brani=albumFile.getListaBrani(); 
    if (indiceVecchio < 0 || indiceVecchio >= brani.size() || indiceNuovo < 0 || indiceNuovo >= brani.size()) { throw new OperazioneNonValidaException("Gli indici specificati non sono validi"); }
    Brano brano=brani.remove(indiceVecchio); 
    brani.add(indiceNuovo, brano); 
    albumFile.setBrano(brani); 
    modificaAlbum(albumFile); 
  }
  @Override 
  public List<Brano> findBrani(Album album) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    Album albumFile=findAlbumByTitolo(album.getTitolo()); 
    return new ArrayList<>(albumFile.getListaBrani()); 
  }
  @Override 
  public int calcolaDurataTotale(Album album) throws BusinessException {
    if (album==null) { throw new OperazioneNonValidaException("L'album non può essere null"); }
    Album albumFile=findAlbumByTitolo(album.getTitolo()); 
    int totale=0; 
    for (Brano brano : albumFile.getListaBrani()) { totale += brano.getDurata(); }
    return totale; 
  }
  private FileData leggiDati() throws IOException { File file=new File(this.filename); 
    if (!file.exists()) { FileData data=new FileData(); 
      data.setContatore(0); 
      data.setRighe(new ArrayList<>()); 
      return data;
    }
    return Utility.readAllRows(this.filename); 
  }
  private List<Album> convertiRighe(FileData data) throws BusinessException { List<Album> album new ArrayList<>(); 
    for (String[] riga : data.getRighe()) {
      Album a=creaAlbumDaRiga(riga); 
      if (a!=null) { album.add(a); }
    }
    return album; 
  }
  private Album creaAlbumDaRiga(String[] riga) throws BusinessException {
    if (riga==null || riga.length < 7) { return null; }
    String titolo=riga[0]; 
    int annoPubblicazione; 
    try { annoPubblicazione=Integer.parseInt(riga[1].trim()); }
    catch (NunmberFormatException e) { throw new BusinessException("Anno di pubblicazione non valido:" + riga[1]); }
    String nomeArtista=riga[2]; 
    Artista artista=trovaArtista(nomeArtista); 
    if (artista==null) { throw new BusinessException("Artista non trovato per l'album:" + nomeArtista); }
    Set<GenereMusicale> generi=new HashSet<>(); 
    if (!riga[3].trim().isEmpty()) { Strin[] generiString=riga[3].split("\\|"); 
      for (String genere : generiString) {
        try { generi.add(GenereMusicale.valueOf(genere.trim())); }
        catch(IllegalArgumentException e) { throw new BusinessException("Genere musicale non valido:" + genere); }
      }
    }
    String copertina=riga[4];
    List<Brano> brani=new ArrayList<>(); 
    if (!riga[5].trim().isEmpty()) { String[] titoliBrani=riga[5].split("\\|"); 
      for (String titoloBrano : titoliBrani) {
        Brano brano=new Brano(titoloBrano.trim(); 0, "", new HashSet<GenereMusicale>(), null, 0); 
        brano.setAlbumBrano(null); 
        brani.add(brano); 
      }
    }
    Album album=new Album(brani, artista, titolo, annoPubblicazione, generi, copertina); 
    return album; 
  }
  private Artista trovaArtista(String nome) throws BusinessException { FileArtistaServiceImpl artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt"); 
    try { return artistaService.findArtistaByNome(nome); }
    catch(ArtistaNonTrovatoException e) { return null; }
  }
  private void scriviAlbum(List<Album> album) throws BusinessException { File file=new File(this.filename); 
    File cartella=file.getParentFile(); 
    if (cartella!=null && !cartella.exists()) { cartella.mkdirs(); }
    try (PrintWriter out=new PrintWriter(file)) { out.println(album.size()); 
      for (Album a : album) {
        out.println(convertiAlbumInRiga(a));
      }
    }
    catch (IOException e) { throw new BusinessException("Errore durante il salvataggio degli album", e); }
  }
  private String convertiAlbumInRiga(Album album) { StringBuilder riga=new StringBuilder(); 
    riga.append(album.getTitolo()); 
    riga.append(","); 
    riga.append(album.getAnnoPubblicazione()); 
    riga.append(","); 
    riga.append(ottieniNomeArtista(album.getArtista())); 
    riga.append(","); 
    String generi=""; 
    for (GenereMusicale genere : album.getGenere()) {
      if (!generi.isEmpty()) { generi +="|"; }
      generi += genere.name(); 
    }
    riga.append(generi); 
    riga.append(","); 
    riga.append(album.getCopertina()); 
    riga.append(",");
    String brani=""; 
    for (Brano brano : album.getListaBrani()) {
      if (!brani.isEmpty()) { brani +="|"; }
      brani += brano.getTitolo(); 
    }
    riga.append(brani); 
    riga.append(","); 
    riga.append(album.getListaBrani().size()); 
    return riga.toString(); 
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) { ArtistaSolista solista=(ArtistaSolista) artista; 
      return solista.getNomeArte(); 
    }
    if (artista instanceof ArtistaGruppo) { ArtistaGruppo gruppo=(ArtistaGruppo) artista; 
      return gruppo.getNomeGruppo(); 
    }
    return ""; 
  }
}
