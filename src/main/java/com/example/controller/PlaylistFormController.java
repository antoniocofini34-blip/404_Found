package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FilePlaylistServiceImpl;
import com.example.domain.Playlist;
import com.example.domain.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
public class PlaylistFormController {
  @FXML
  private TextField campoNome;
  @FXML
  private TextArea campoDescrizione;
  @FXML
  private Label messaggioLabel;
  private FilePlaylistServiceImpl playlistService;
  private Playlist playlistDaModificare;
  private String nomeVecchio;
  @FXML
  private void initialize() {
    playlistService=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt");
  }
  public void impostaPlaylist(Playlist playlist) {
    playlistDaModificare=playlist;
    nomeVecchio=playlist.getNome();
    campoNome.setText(playlist.getNome());
    campoDescrizione.setText(playlist.getDescrizione());
  }
  @FXML
  private void salva() throws IOException {
    String nome=campoNome.getText();
    String descrizione=campoDescrizione.getText();
    if (nome==null || nome.trim().isEmpty()) {
      messaggioLabel.setText("Inserisci il nome della playlist.");
      return;
    }
    if (descrizione==null) descrizione="";
    try {
      if (playlistDaModificare==null) {
        Utente utente=UtenteAttivo.getUtente();
        if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) {
          messaggioLabel.setText("Devi essere autenticato per creare una playlist.");
          return;
        }
        Playlist playlist=new Playlist(nome.trim(), descrizione.trim(), LocalDate.now(), utente.getEmail().trim());
        playlistService.creaPlaylist(playlist);
      } else {
        playlistDaModificare.setNome(nome.trim());
        playlistDaModificare.setDescrizione(descrizione.trim());
        playlistService.modificaPlaylist(nomeVecchio, playlistDaModificare);
      }
      tornaAllaPlaylist();
    } catch(BusinessException e) { messaggioLabel.setText("Errore nel salvataggio: " + e.getMessage()); }
  }
  @FXML
  private void annulla() throws IOException {
    tornaAllaPlaylist();
  }
  private void tornaAllaPlaylist() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage) campoNome.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
}
