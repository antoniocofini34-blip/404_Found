import java.util.*;
public class Utente {
  private String nome;
  private String cognome;
  private String email;
  private LocalDate data_di_nascita;
  private List<Playlist> playlist_Create;
  private Cronologia_Ascolti cronologia_Ascolti;
  private Set<Genere> preferenze_musicali;
  public Utente(String nome, String cognome, String email, LocalDate data_di_nascita, List<Playlist> playlist_Creata, Cronologia_Ascolti cronologia_Ascolti, Set<Genere> preferenze_musicali) {
    this.nome=nome;
    this.cognome=cognome;
    this.email=email;
    this.data_di_nascita=data_di_nascita;
    this.playlist_Creata=new ArrayList<>();
    this.cronologia_Ascolti=new Cronologia_Ascolti();
    this.preferenze_musicali=new HashSet<>();
  }
  public String getNome() {
    return this.nome;
  }
  public String getCognome() {
    return this.cognome;
  }
  public String getEmail() {
    return this.email;
  }
  public LocalDate getDataDiNascita() {
    return this.data_di_nascita;
  }
  public List<Playlist> getPlaylistCreate() {
    return this.playlist_Creata;
  }
  public Cronologia_Ascolti getCronologiaAscolti() {
    return this.cronologia_Ascolti;
  }
  public Set<Genere> getGeneriMusicali() {
    return this.preferenze_musicali;
  }
  public void setNome(String nome) {
    this.nome=nome;
  }
  public void setCognome(String cognome) {
    this.cognome=cognome;
  }
  public void setEmail(String email) {
    this.email=email;
  }
  public void setDataDiNascita(LocalDate data_di_nascita) {
    this.data_di_nascita=data_di_nascita;
  }
  public void setPlaylistCreate(List<Playlist> playlist_Create) {
    this.playlist_Create=playlist_Create;
  }
  public void setCronologiaAscolti(Cronologia_Ascolti cronologia_Ascolti) {
    this.cronologia_Ascolti=cronologia_Ascolti;
  }
  public void setPreferenzeMusicali(Set<Genere> preferenze_musicali) {
    this.preferenze_musicali=preferenze_musicali;
  }
  public Playlist creaPlaylist(String nome, String descrizione) {
    Playlist playlist=new Playlist(nome,descrizione);
    playlist_Create.add(playlist);
    return playlist;
  }
  public void eliminaPlaylist(Playlist playlist) {
    playlist_Create.remove(playlist);
  }
  public void aggiungiPreferenza(Genere genere) {
    preferenze_musicali.add(genere);
  }
  public void rimuoviPreferenza(Genere genere) {
    preferenze_musicali.remove(genere);
  }
  public void avviaPlaylist(Playlist playlist) {
    playlist.play();
  }
  public void avviaBrano(Brano brano) {
    brano.play();
  }
  public List<Brano> ottieniSuggerimenti() {
    return new ArrayList<>();
  }
}
