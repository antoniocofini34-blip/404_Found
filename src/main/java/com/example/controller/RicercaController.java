package com.example.controller;
import com.example.business.BusinessException;
import com.example.business.RicercaService;
import com.example.business.impl.file.FIleAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileRicercaServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.Brano;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RicercaController{
  @FXML
  private TextField campoRicerca;
  @FXML
  private Label risultatoLabel;
  private RicercaService ricercaService;
  @FXML
  private void initialize(){
    FileBranoServiceImpl branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    FileArtistaServiceImpl artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artista.txt");
    ricercaService=new FileRicercaServiceImpl(branoService, albumService, artistaService);
  }
  @FXML
  private void cerca(){
    String testo=campoRicerca.getText();
    if(testo==null||testo.trim().isEmpty()){
      risultatoLabel.setText("Inserisci qualcosa da cercare");
      return;
    }
    try{
      List<Brano>brani=ricercaService.cercaBrani(testo);
      List<Album>album=ricercaService.cercaAlbum(testo);
      List<Artista>artista=ricercaServic.cercaArtisti(testo);
      StringBuilder risultato=new StringBuilder();
      risultato.append("Risultati trovati:\n\n");
      risultato.append("Brani: ").append(brani.size()).append("\n");
      for (Brano brano : brani) {
        risultato.append("- ").append(brano.getTitolo()).append("\n");
      }
      risultato.append("\nAlbum: ").append(album.size()).append("\n");
      for (Album a : album) {
        risultato.append("- ").append(a.getTitolo()).append("\n");
      }
      risultato.append("\nArtisti: ").append(artisti.size()).append("\n");
      for (Artista artista : artisti) {
        risultato.append("- Artista trovato\n");
      }
        risultatoLabel.setText(risultato.toString());
      } catch (BusinessException e) {
      risultatoLabel.setText("Errore nella ricerca: " + e.getMessage());
      }
  }
  @FXML
  private void tornaIndietro() throws IOException{
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage)campoRicerca.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
}
