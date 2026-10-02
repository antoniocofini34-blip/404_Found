package com.example.domain;
import java.util.*;
import java.time.*;

public class Utente {
  private String nome;
  private String cognome;
  private String email;
  private LocalDate data_di_nascita;
  private List<Playlist> playlist_Create;
  private Cronologia_Ascolti cronologia_Ascolti;
  private Set<GenereMusicale> preferenze_musicali;
  private CodaRiproduzione coda_riproduzione; 
  public Utente(String nome, String cognome, String email, LocalDate data_di_nascita, List<Playlist> playlist_Creata, Cronologia_Ascolti cronologia_Ascolti, Set<GenereMusicale> preferenze_musicali, CodaRiproduzione coda_riproduzione) {
    this.nome=nome;
    this.cognome=cognome;
    this.email=email;
    this.data_di_nascita=data_di_nascita;
    if (playlist_Create!=null) this.playlist_Create=new ArrayList<>(playlist_Create); 
    else this.playlist_Create=new ArrayList<>(); 
    this.cronologia_Ascolti=cronologia_Ascolti;
    if (preferenze_musicali!=null) this.preferenze_musicali=new HashSet<>(preferenze_musicali); 
    else this.preferenze_musicali=new HashSet<>(); 
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
    return new ArrayList<>(this.playlist_Create);
  }
  public Cronologia_Ascolti getCronologiaAscolti() {
    return this.cronologia_Ascolti;
  }
  public Set<GenereMusicale> getGeneriMusicali() {
    return new HashSet<>(this.preferenze_musicali);
  }
  public CodaRiproduzione getCodaRiproduzione() {
    return this.coda_riproduzione; 
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
    if (playlist_Create!=null) this.playlist_Create=new ArrayList<>(playlist_Create); 
    else this.playlist_Create=new ArrayList<>(); 
  }
  public void setCronologiaAscolti(Cronologia_Ascolti cronologia_Ascolti) {
    this.cronologia_Ascolti=cronologia_Ascolti;
  }
  public void setPreferenzeMusicali(Set<GenereMusicale> preferenze_musicali) {
    if (preferenze_musicali!=null) this.preferenze_musicali=new HashSet<>(preferenze_musicali); 
    else this.preferenze_musicali=preferenze_musicali; 
  }
  public void setCodaRiproduzione(CodaRiproduzione coda_riproduzione) {
    this.coda_riproduzione=coda_riproduzione;
  }
}
