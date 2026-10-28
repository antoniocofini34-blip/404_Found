package com.example.domain;
public class Componente {
  private String nome;
  private String cognome;
  private String ruolo;
  public Componente(String nome, String cognome, String ruolo) {
    this.nome=nome;
    this.cognome=cognome;
    this.ruolo=ruolo;
  }
  public String getNome() { return nome; }
  public String getCognome() { return cognome; }
  public String getRuolo() { return ruolo; }
  public void setNome(String nome) { this.nome=nome; }
  public void setCognome(String cognome) { this.cognome=cognome; }
  public void setRuolo(String ruolo) { this.ruolo=ruolo; }
  @Override
  public String toString() { return nome + " " + cognome; }
}
