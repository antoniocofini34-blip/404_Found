import java.util.*;
public class ArtistaSolista extends Artista {
  private String nome_arte;
  public Artista_Solista (String biografia, Genere genere_principale, Set<Genere> generi_secondari, List<Album> discografia) {
    super(biografia, genere_principale, generi_secondari, discografia);
  }
  public String getNomeArte() {
    return nome_arte;
  }
  public void setNomeArte(String nome_arte) {
    this.nome_arte=nome_arte;
}
}
