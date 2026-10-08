package com.example.business.impl.file;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.example.business.BusinessException;
import com.example.business.UtenteService;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.CodaRiproduzione;
import com.example.domain.GenereMusicale;
import com.example.domain.Playlist;
import com.example.domain.Utente;
public class FileUtenteServiceImpl implements UtenteService{
  private String filename;
  private FilePlaylistServiceImpl playlistService;
  public FileUtenteServiceImpl(String filename){
    this.filename=filename;
    this.playlistService=new FilePlaylistServiceImpl("src/main/resource/dati/playlist.txt");
  }
  @Override
  public void creaUtente(Utente utente) throws BusinessException{
    if(utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if(utente.getEmail()==null||utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota");
    List<Utente>utenti=findAllUtenti();
    for(Utente u : utenti){
      if(u.getEmail()!=null && u.getEmail.equalsIgnoreCasa(utente.getEmail().trim())) throw new DuplicatoException("Esiste già un utente con email:" + utente.getEmail());
    }
    utenti.add(utente);
    scriviUtenti(utenti);
  }
  @Override
  public void modificaUtente(Utente utente) throws BusinessException{
    if(utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if(utente.getEmail()==null||utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota");
    List<Utente>utenti=findAllUtenti();
    boolean trovato=false;
    for(int i=0; i<utenti.size(); i++){
      Utente utenteEsistente=utenti.get(i);
      if(utenteEsistente.getEmail()!=null && utenteEsistente.getEmail().equalsIgnoreCase(utente.getEmail().trim())){
        utente.set(i, utente);
        trovato=true;
        break;
      }
    }
    if(!trovato) throw new OperazioneNonValidaException("Utente da modificare non trovato");
    scriviUtenti(utenti);
  }
  @Override
   public void eliminaUtente(Utente utente) throws BusinessException{
    if(utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if(utente.getEmail()==null||utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non è valida");
    List<Utente>utenti=findAllUtenti();
    boolean rimosso=false;
    for(int i=0; i<utenti.size(); i++){
      Utente utenteEsistente=utenti.get(i);
      if(utenteEsistente.getEmail()!=null && utenteEsistente.getEmail().equalsIgnoreCase(utente.getEmail().trim())){
        utente.remove(i);
        rimosso=true;
        break;
      }
    }
    if(!rimosso) throw new OperazioneNonValidaException("Utente da eliminare non trovato");
    scriviUtenti(utenti);
  }
  @Override
  public List<Utente> findAllUtenti() throws BusinessException{
    try{
      FileData data=leggiDati();
      return convertiRighe(data);
    } catch(IOException e) throw new BusinessException("Errore durante la lettura degli utenti", e);
  }
  @Override
  public Utente findUtenteByEmail(String email) throws BusinessException{
    if(email==null||email.trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non è valida");
    List<Utente>utenti=findAllUtenti();
    for(Utente utente : utenti){
      if(utente.getEmail()!=null && utente.getEmail().equalsIgnoreCase(email.trim())) return utente;
    }
      throw new OperazioneNonValidaException("Utente non trovato:" + email);
    }
    @Override
    public void aggiungiPlaylist(Utente utente, Playlist playlist) throws BusinessException{
    if(utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if(playlist==null) throw new OperazioneNonValidaException("La playlist non può essere null");
    Utente utenteFile =findUtenteByEmail(utente.getEmail());
    List<Playlist> playlistUtente =new ArrayList<>(utenteFile.getPlaylistCreate());
        for (Playlist p :playlistUtente) {
            if (p.getNome() != null && p.getNome().equalsIgnoreCase(playlist.getNome())) throw new DuplicatoException("La playlist è già associata all'utente");
        }
        Playlist playlistFile = playlistService.findPlaylistByNome(playlist.getNome());
        playlistUtente.add(playlistFile);
        utenteFile.setPlaylistCreate(playlistUtente);
        modificaUtente(utenteFile);
     }
     public void rimuoviPlaylist(Utente utente, Playlist playlist) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        if (playlist == null) throw new OperazioneNonValidaException("La playlist non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        List<Playlist> playlistUtente = new ArrayList<>(utenteFile.getPlaylistCreate());
        boolean rimossa = false;
        for (int i = 0; i < playlistUtente.size(); i++) {
            Playlist p = playlistUtente.get(i);
            if (p.getNome() != null && p.getNome().equalsIgnoreCase(playlist.getNome())) {
                playlistUtente.remove(i);
                rimossa = true;
                break;
            }
        }
        if (!rimossa) throw new OperazioneNonValidaException("La playlist non è associata all'utente");
        utenteFile.setPlaylistCreate(playlistUtente);
        modificaUtente(utenteFile);
    }
    @Override
    public List<Playlist> getPlaylist(Utente utente) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        return new ArrayList<>(utenteFile.getPlaylistCreate());
    }
    @Override
    public void aggiungiPreferenza(Utente utente, GenereMusicale genere) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        if (genere == null) throw new OperazioneNonValidaException("Il genere musicale non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        Set<GenereMusicale> preferenze = new HashSet<>(utenteFile.getGeneriMusicali());
        if (!preferenze.add(genere)) throw new DuplicatoException("La preferenza è già presente");
        utenteFile.setPreferenzeMusicali(preferenze);
        modificaUtente(utenteFile);
    }
    @Override
    public void rimuoviPreferenza(Utente utente, GenereMusicale genere) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
         if (genere == null) throw new OperazioneNonValidaException("Il genere musicale non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        Set<GenereMusicale> preferenze = new HashSet<>(utenteFile.getGeneriMusicali());
        if (!preferenze.remove(genere)) throw new OperazioneNonValidaException("La preferenza non è presente");
        utenteFile.setPreferenzeMusicali(preferenze);
        modificaUtente(utenteFile);
    }
    @Override
    public Set<GenereMusicale> getPreferenze(Utente utente) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        Utente utenteFile =findUtenteByEmail(utente.getEmail());
        return new HashSet<>(utenteFile.getGeneriMusicali());
    }
    @Override
    public CodaRiproduzione getCodaRiproduzione(Utente utente) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        return utenteFile.getCodaRiproduzione();
    }
    @Override
    public void setCodaRiproduzione(Utente utente, CodaRiproduzione coda) throws BusinessException {
        if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
        Utente utenteFile = findUtenteByEmail(utente.getEmail());
        utenteFile.setCodaRiproduzione(coda);
        modificaUtente(utenteFile);
    }
    private FileData leggiDati() throws IOException { File file = new File(this.filename);
        if (!file.exists()) {
            FileData data = new FileData();
            data.setContatore(0);
            data.setRighe(new ArrayList<>());
            return data;
        }
        return Utility.readAllRows(this.filename);
    }
    private List<Utente> convertiRighe(FileData data) throws BusinessException { List<Utente> utenti = new ArrayList<>();
        for (String[] riga : data.getRighe()) {
            Utente utente = creaUtenteDaRiga(riga);
            if (utente != null) utenti.add(utente);
        }
        return utenti;
    }
    private Utente creaUtenteDaRiga(String[] riga) throws BusinessException {
        if (riga == null || riga.length < 6) return null;
        String nome =vriga[0];
        String cognome =riga[1];
        String email =riga[2];
        LocalDate dataNascita;
        try {dataNascita =LocalDate.parse(riga[3].trim()); } catch (Exception e) throw new BusinessException("Data di nascita non valida: " + riga[3]);
        List<Playlist> playlist =new ArrayList<>();
        if (!riga[4].trim().isEmpty()) {
            String[] nomiPlaylist =riga[4].split("\\|");
            for (String nomePlaylist :nomiPlaylist) {
                Playlist p = playlistService.findPlaylistByNome(nomePlaylist.trim());
                playlist.add(p);
            }
        }
        Set<GenereMusicale> preferenze =new HashSet<>();
        if (!riga[5].trim().isEmpty()) {
            String[] generi =riga[5].split("\\|");
            for (String genere :generi) {
                if (!genere.trim().isEmpty()) {
                    try { preferenze.add(GenereMusicale.valueOf(genere.trim().toUpperCase())); } catch (IllegalArgumentException e) throw new BusinessException("Genere musicale non valido: " + genere);
                }
            }
        }
        Utente utente = new Utente(nome,cognome,email, dataNascita, playlist, null, preferenze, null);
        utente.setPreferenzeMusicali(preferenze);
        return utente;
    }
    private void scriviUtenti(List<Utente> utenti) throws BusinessException { File file =new File(this.filename);
        File cartella =file.getParentFile();
        if (cartella != null && !cartella.exists()) cartella.mkdirs();
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(utenti.size());
            for (Utente utente : utenti) {
                out.println(convertiUtenteInRiga(utente));
            }
        } catch (IOException e) throw new BusinessException("Errore durante il salvataggio degli utenti", e);
    }
    private String convertiUtenteInRiga(Utente utente){ StringBuilder riga =new StringBuilder();
        riga.append(utente.getNome());
        riga.append(",");
        riga.append(utente.getCognome());
        riga.append(",");
        riga.append(utente.getEmail());
        riga.append(",");
        riga.append(utente.getDataDiNascita());
        riga.append(",");
        String playlist = "";
        for (Playlist p : utente.getPlaylistCreate()) {
            if (!playlist.isEmpty()) playlist += "|";
            playlist += p.getNome();
        }
        riga.append(playlist);
        riga.append(",");
        String preferenze = "";
        for (GenereMusicale genere : utente.getGeneriMusicali()) {
                if (!preferenze.isEmpty()) preferenze += "|";
            preferenze += genere.name();
        }
        riga.append(preferenze);
        return riga.toString();
    }
}
