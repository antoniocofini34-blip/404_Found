package com.example.domain;
public record RisultatoRicerca(Brano brano, Album album, Artista artista) {
  public RisultatoRicerca(Brano brano) { this(brano, null, null); }
  public RisultatoRicerca(Album album) { this(null, album, null); }
  public RisultatoRicerca(Artista artista)  { this(null, null, artista); }
  public String getTipo() { 
    if (brano!=null) return "Brano";
    if (album!=null) return "Album";
    return "Artista";
  }
  public String getTitolo() { 
    if (brano!=null) return brano.getTitolo();
    if (album!=null) return album.getTitolo(); 
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "Artista";
  }
  public String getDurata() { return brano!=null ? brano.getDurata() + "sec" : "-"; }
  public String getGenere() { 
    if (brano!=null) return brano.getGenereBrano().toString();
    if (album!=null) return album.getGenere().toString(); 
    return artista!=null ? artista.getGenerePrincipale().toString() : "-";
  }
  public int getAnnoPubblicazione() {
    if (brano!=null && brano.getAlbumBrano()!=null) return brano.getAlbumBrano().getAnnoPubblicazione();
    return album!=null ? album.getAnnoPubblicazione() : 0; 
  }
  public int getNumeroAscolti() { return brano!=null ? brano.getNumeroAscolti() : 0; }
}
