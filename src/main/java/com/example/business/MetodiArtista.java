  public void modificaBiografia(String biografia) {
    this.biografia=biografia;
  }
  public void setGenerePrincipale(Genere genere_principale) {
    this.genere_principale=genere_principale;
  }
  public void aggiungiGeneriSecondari(Genere generi_secondari) {
    this.generi_secondari.add(generi_secondari); 
  }
  public void rimuoviGeneriSecondari(Genere generi_secondari) {
    this.generi_secondari.remove(generi_secondari);
  }
  public void aggiungiAlbum(Album album) {
    this.album.add(album); 
  }
  public void rimuoviAlbum(Album album) {
    this.album.remove(album); 
  }
