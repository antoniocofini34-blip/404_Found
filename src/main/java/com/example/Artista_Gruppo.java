import java.util.*;
public class Artista_Gruppo extends Artista implements Ricercabile {
  private String nome_gruppo;
  private List<Artista> lista_Membri;
  public Artista_Gruppo(String nome_gruppo, List<Artista> lista_Membri) {
    this.nome_gruppo=nome_gruppo;
    this.lista_Membri=new ArrayList<>();
  }
  public String getNomeGruppo() {
    return nome_gruppo;
  }
  public List<Artista> getLista() {
    return lista_membri;
  }
  public void setNomeGruppo(String nome_gruppo) {
    this.nome_gruppo=nome_gruppo;
  }
  public void setListMembri(List<Artista> lista_Membri) {
    this.lista_Membri=lista_Membri;
  }
  public void aggiungiComponente(Artista artista) {
    lista.Membri.add(artista);
  }
  public void rimuoviComponente(Artista artista) {
    lista_Membri.remove(artista);
  }
  public List<Artista> getComponenti() {
    return lista_Membri;
  }
  @Override
  public boolean corrispondeA(String testo) {
    return nome_gruppo.toLowerCase().contains(testo.ToLowerCase());
  }
  @Override
  public String getNome() {
    return nome_gruppo;
  }
}
