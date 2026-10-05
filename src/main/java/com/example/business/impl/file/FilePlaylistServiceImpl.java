package com.example.business.impl.file; 
import java.io.File; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.*; 
import java.time.*; 
import com.example.business.BusinessException; 
import com.example.business.PlaylistService; 
import com.example.business.exception.DuplicatoException; 
import com.example.business.exception.OperazioneNonValidaException; 
import com.example.domain.Brano; 
import com.example.domain.Playlist; 

public class FilePlaylistServiceImpl implements PlaylistService {
  private String filename; 
  private FileBranoServiceImpl branoService; 
  public FilePlaylistServiceImpl(String filename) {
    this.filename=filename; 
    this.branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt"); 
  }
  @Override 
  public void creaPlaylist(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    if (playlist.getNome()==null || playlist.getNome().trim().isEmpty()) {
      throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto"); 
    }
    List<Playlist> playlistEsistenti=findAllPlaylist(); 
    for (Playlist p : playlistEsistenti) {
      if (p.getNome()!=null && p.getNome().equalsIgnoreCase(playlist.getNome().trim())) {
        throw new DuplicatoException("Esiste già una playlist con il nome:" + playlist.getNome()); 
      }
    }
    playlistEsistenti.add(playlist); 
    scriviPlaylist(playlistEsistenti); 
  }
  @Override 
  public void modificaPlaylistpublic(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    if (playlist.getNome()==null || playlist.getNome().trim().isEmpty()) {
      throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto"); 
    }
    List<Playlist> playlistEsistenti=findAllPlaylist(); 
    boolean trovata=false; 
    for (int i=0; i < playlistEsistenti.size(); i++) {
      Playlist playlistEsistente=playlistEsistenti.get(i); 
      if (playlistEsistente.getNome()!=null && playlistEsistente.getNome().equalsIgnoreCase(playlist.getNome().trim())) {
        playlistEsistenti.set(i, playlist); 
        trovato=true; 
        break; 
      }
    }
    if (!trovato) {
      throw new OperazioneNonValidaException("Playlist da modificare non trovata"); 
    }
    scriviPlaylist(playlistEsistenti); 
  }
  @Override
  public void eliminaPlaylist(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    if (playlist.getNome()==null || playlist.getNome().trim().isEmpty()) {
      throw new OperazioneNonValidaException("Il nome della playlist non può essere vuoto"); 
    }
    List<Playlist> playlistEsistenti=findAllPlaylist(); 
    boolean rimossa=false; 
    for (int i=0; i < playlistEsistenti.size(); i++) {
      Playlist playlistEsistente=playlistEsistenti.get(i); 
      if (playlistEsistente.getNome()!=null && playlistEsistente.getNome().equalsIgnoreCase(playlist.getNome().trim())) {
        playlistEsistenti.remove(i); 
        rimossa=true; 
        break; 
      }
    }
    if (!rimossa) {
      throw new OperazioneNonValidaException("Playlist da eliminare non trovata"); 
    }
    scriviPlaylist(playlistEsistenti); 
  }
  @Override 
  public List<Playlist> findAllPlaylist() throws BusinessException {
    try {
      FileData data=leggiDati(); 
      return convertiRighe(data); 
    }
    catch(IOException e) {
      throw new BusinessException("Errore durante la lettura della playlist", e); 
    }
  }
  @Override
  public Playlist findPlaylistByNome(String nome) throws BusinessException {
    if (nome==null || nome.trim.isEmpty()) {
      throw new OperazioneNonValidaException("Il nome della playlist non è valido"); 
    }
    List<Playlist> playlist=findAllPlaylist(); 
    for (Playlist p : playlist) {
      if (p.getNome()!=null && p.getNome().equalsIgnoreCase(nome.trim())) {
        return p; 
      }
    }
    throw new OperazioneNonValidaException("Playlist non trovata:" + nome); 
  }
  @Override 
  public void aggiungiBrano(Playlist playlist, Brano brano) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    if (brano==null) {
      throw new OperazioneNonValidaException("Il brano non può essere null"); 
    }
    Playlist playlistFile=finPlaylistByNome(playlist.getNome()); 
    Brano branoFile=branoService.findBranoByTitolo(brano.getTitolo()); 
    List<Brano> brani=playlistFile.getLista_brani(); 
    for (Brano b : brani) {
      if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(branoFile.getTitolo())) {
        throw new DuplicatoException("Il brano è già presente nella playlist"); 
      }
    }
    brani.add(branoFile); 
    playlistFile.setLista_brani(brani); 
    modificaPlaylist(playlistFile); 
  }
  @Override
  public void rimuoviBrano(Playlist playlist, Brano brano) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    if (brano==null) {
      throw new OperazioneNonValidaException("Il brano non può essere null"); 
    }
    Playlist playlistFile=findPlaylistByNome(playlist.getNome()); 
    List<Brano> brani=playlistFile.getLista_brani(); 
    boolean rimosso=false; 
    for (int i=0; i < brani.size(); i++) {
      Brano b=brani.get(i); 
      if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo())) {
        brani.remove(i); 
        rimosso=true; 
        break; 
      }
    }
    if (!rimosso) {
      throw new OperazioneNonValidaException("Il brano non è presente nella playlist"); 
    }
    playlistFile.setLista_brani(brani); 
    modificaPlaylist(playlistFile); 
  }
  @Override 
  public void riordinaBrani(Playlist playlist, int indiceVecchio, int indiceNuovo) throws BusinessException {
    if (playlist))null) {
      throw new OperazioneNonValidaException("La playlist non può essere null"); 
    }
    Playlist playlistFile=findPlaylistByNome(playlist.getNome()); 
    List<Brano> brani=playlistFile.getLista_brani(); 
    if (indiceVecchio < 0 || indiceVecchio >= brani.size() || indiceNuovo < 0 || indiceNuovo >= brani.size() {
      throw new OperazioneNonValidaException("Gli indici specificati non sono validi"); 
    }
    Brano brano=brani.remove(indiceVecchio); 
    brani.add(indiceNuovo, brano); 
    playlistFile.setLista_brani(brani); 
    modificaPlaylist(playlistFile); 
  }
  @Override 
  public List<Brano> getBrani(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValida("La playlist non può essere null"); 
    }
    Playlist playlistFile=findPlaylistByNome(playlist.getNome()); 
    return new ArrayList<>(playlistFile.getLista_brani()); 
  }
  @Override 
  public int calcolaDurataTotale(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValida("La playlist non può essere null"); 
    }
    Playlist playlistFile=findPlaylistByNome(playlist.getNome()); 
    int totale=0; 
    for (Brano brano : playlistFile.getLista_brani()) {
      totale += brano.getDurata(); 
    }
    return totale; 
  }
  @Override 
  public void riproduciPlaylist(Playlist playlist) throws BusinessException {
    if (playlist==null) {
      throw new OperazioneNonValida("La playlist non può essere null"); 
    }
    List<Brano> brani=getBrani(playlist); 
    if (brani.isEmpty()) {
      throw new OperazioneNonValidaException("La playlist è vuota"); 
    }
    playlist.setIndiceCorrente(0); 
  }
  private FileData leggiDati() throws IOException {
    File file=new File(this.filename); 
    if (!file.exists()) {
      FileData data=new FileData(); 
      data.setContatore(0); 
      data.setRighe(new ArrayList<>()); 
      return data;
    }
    return Utility.readAllRows(this.filename); 
  }
  private List<Playlist> convertiRighe(FileData data) throws BusinessException {
    List<Playlist> playlist= new ArrayList<>(); 
    for (String[] riga : data.getRighe()) {
      Playlist p=creaPlaylistDaRiga(riga); 
      if (p!=null) {
        playlist.add(p); 
      }
    }
    return playlist; 
  }
  private Playlist creaPlaylistDaRiga(String[] riga) throws BusinessException {
    if (riga==null || riga.length < 4) {
      return null; 
    }
    String nome=riga[0]; 
    String descrizione=riga[1]; 
    LocalDate dataCreazione; 
    try {
      dataCreazione=LocalDate.parse(riga[2].trim()); 
    }
    catch(Exception e) {
      throw new BusinessException("Data di creazione non valida:" + riga[2]; 
    }
    Playlist playlist=new Playlist(nome, descrizione, dataCreazione, -1, 0, 0); 
    if (!riga[3].trim().isEmpty()) {
      String[] titoliBrani=riga[3].split("\\|"); 
      List<Brano> brani=new ArrayList<>(); 
      for (String titoloBrano : titoliBrani) {
        Brano brano=branoService.findBranoByTitolo(titoloBrano.trim()); 
      }
      playlist.setLista_brani(brani); 
    }
    return playlist; 
  }
  private void scriviPlaylist(List<Playlist> playlist) throws BusinessException {
    File file=new File(this.filename); 
    File cartella=file.getParentFile(); 
    if (cartella!=null && !cartella.exists()) {
      cartella.mkdirs(); 
    }
    try (PrintWriter out=new PrintWriter(file)) {
      out.println(playlist.size()); 
      for (Playlist p : playlist) {
        out.println(convertiPlaylistInRiga(p)); 
      }
      catch(IOException e) {
      throw new BusinessException("Errore durante il salvataggio delle playlist"); 
    }
  }
  private String convertiPlaylistInRiga(Playlist playlist) {
    StringBuilder riga=new StringBuilder(); 
    riga.append(playlist.getNome()); 
    riga.append(","); 
    riga.append(playlist.getDescrizione()); 
    riga.append(",");
    riga.append(playlist.getData_creazione()); 
    riga.append(",");
    String brani=""; 
    for (Brano brano : playlist.getLista_brani()) {
      if (!brani.isEmpty()) {
        brani +="|"; 
      }
      brani +=brano.getTitolo(); 
    }
    riga.append(brani); 
    return riga.toString(); 
  }
}
