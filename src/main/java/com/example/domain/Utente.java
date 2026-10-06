package com.example.domain;
import java.util.*;
import java.time.*;

public class Utente {
  private String nome;
  private String cognome;
  private String email;
  private LocalDate data_di_nascita;
  private List<Playlist> playlist_create;
  private Cronologia_Ascolti cronologia_ascolti;
  private Set<GenereMusicale> preferenze_musicali;
  private CodaRiproduzione coda_riproduzione; 
  public Utente(String nome, String cognome, String email, LocalDate data_di_nascita, List<Playlist> playlist_creata, Cronologia_Ascolti cronologia_ascolti, Set<GenereMusicale> preferenze_musicali, CodaRiproduzione coda_riproduzione) {
    this.nome=nome;
    this.cognome=cognome;
    this.email=email;
    this.data_di_nascita=data_di_nascita;
    if (playlist_create!=null) this.playlist_create=new ArrayList<>(playlist_create); 
    else this.playlist_create=new ArrayList<>(); 
    this.cronologia_ascolti=cronologia_ascolti;
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
    return new ArrayList<>(this.playlist_create);
  }
  public Cronologia_Ascolti getCronologiaAscolti() {
    return this.cronologia_ascolti;
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
  public void setPlaylistCreate(List<Playlist> playlist_create) {
    if (playlist_create!=null) this.playlist_create=new ArrayList<>(playlist_create); 
    else this.playlist_create=new ArrayList<>(); 
  }
  public void setCronologiaAscolti(Cronologia_Ascolti cronologia_ascolti) {
    this.cronologia_ascolti=cronologia_ascolti;
  }
  public void setPreferenzeMusicali(Set<GenereMusicale> preferenze_musicali) {
    if (preferenze_musicali!=null) this.preferenze_musicali=new HashSet<>(preferenze_musicali); 
    else this.preferenze_musicali=preferenze_musicali; 
  }
  public void setCodaRiproduzione(CodaRiproduzione coda_riproduzione) {
    this.coda_riproduzione=coda_riproduzione;
  }
}
