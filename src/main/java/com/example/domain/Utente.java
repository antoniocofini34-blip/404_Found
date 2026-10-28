package com.example.domain;
import java.util.*;
import java.time.*;
public class Utente { private String nome;
  private String cognome;
  private String email;
  private LocalDate data_di_nascita;
  private List<Playlist> playlist_create;
  private Set<GenereMusicale> preferenze_musicali;
  private CodaRiproduzione coda_riproduzione; 
  public Utente(String nome, String cognome, String email, LocalDate data_di_nascita, List<Playlist> playlist_create, Set<GenereMusicale> preferenze_musicali, CodaRiproduzione coda_riproduzione) {
    this.nome=nome;
    this.cognome=cognome;
    this.email=email;
    this.data_di_nascita=data_di_nascita;
    if (playlist_create!=null) this.playlist_create=new ArrayList<>(playlist_create); 
    else this.playlist_create=new ArrayList<>(); 
    if (preferenze_musicali!=null) this.preferenze_musicali=new HashSet<>(preferenze_musicali); 
    else this.preferenze_musicali=new HashSet<>();
    this.coda_riproduzione=coda_riproduzione;
  }
  public String getNome() { return this.nome; }
  public String getCognome() { return this.cognome; }
  public String getEmail() { return this.email; }
  public LocalDate getDataDiNascita() { return this.data_di_nascita; }
  public List<Playlist> getPlaylistCreate() { return new ArrayList<>(this.playlist_create); }
  public Set<GenereMusicale> getPreferenzeMusicali() { return new HashSet<>(this.preferenze_musicali); }
  public void setNome(String nome) { this.nome=nome; }
  public void setCognome(String cognome) { this.cognome=cognome; }
  public void setDataDiNascita(LocalDate data_di_nascita) { this.data_di_nascita=data_di_nascita; }
  public void setPreferenzeMusicali(Set<GenereMusicale> preferenze_musicali) {
    if (preferenze_musicali!=null) this.preferenze_musicali=new HashSet<>(preferenze_musicali); 
    else this.preferenze_musicali=preferenze_musicali; 
  }
}
