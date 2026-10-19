package com.example.controller; 
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo; 
import com.example.domain.ArtistaSolista;
import com.example.domain.Brano;
import javafx.collections.FXCollections;
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.TableColumn; 
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
import java.io.IOException; 
import java.util.*; 
public class LibreriaController {
  @FXML
  private TextField campoFiltro; 
  @FXML
  private TableView<Artista> tabellaArtisti; 
  @FXML
  private TableColumn<Artista, String> colonnaNome; 
  @FXML
  private TableColumn<Artista, String> colonnaGenere;
  @FXML
  private TableColumn<Artista, String> colonnaTipo;
  @FXML
  private TableView<Album> tabellaAlbum;
  @FXML
  private TableColumn<Album, String> colonnaTitoloAlbum;
  @FXML
  private TableColumn<Album, String> colonnaAnnoAlbum;
  @FXML
  private TableColumn<Album, String> colonnaArtistaAlbum;
  @FXML
  private TableView<Brano> tabellaBrani;
  @FXML
  private TableColumn<Brano, String> colonnaTitoloBrano;
  @FXML
  private TableColumn<Brano, String> colonnaDurataBrano;
  @FXML
  private TableColumn<Brano, String> colonnaAlbumBrano;
  @FXML
  private Button bottoneIndietro;
  private FileArtistaServiceImpl artistaService;
  private FileAlbumServiceImpl albumService;
  private FileBranoServiceImpl branoService;
  private List<Artista> tuttiGliArtisti;
  private List<Album> tuttiGliAlbum;
  private List<Brano> tuttiIBrani;
  @FXML
  private void initialize() {
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    configuraTabellaArtisti();
    configuraTabellaAlbum();
    configuraTabellaBrani();
    caricaArtisti();
    caricaAlbum();
    caricaBrani();
    configuraSelezioneArtista();
    configuraSelezioneAlbum();
    configuraFiltro();
  }  
  private void configuraTabellaArtisti() {
    colonnaNome.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(ottieniNomeArtista(cellData.getValue())));
    colonnaGenere.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(ottieniGenereArtista(cellData.getValue())));
    colonnaTipo.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(ottieniTipoArtista(cellData.getValue())));
  }
  private void configuraTabellaAlbum() {
    colonnaTitoloAlbum.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(cellData.getValue().getTitolo()));
    colonnaAnnoAlbum.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAnnoPubblicazione())));
    colonnaArtistaAlbum.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(ottieniNomeArtista(cellData.getValue().getArtista())));
  }
  private void configuraTabellaBrani() {
    colonnaTitoloBrano.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(cellData.getValue().getTitolo()));
    colonnaDurataBrano.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDurata())));
    colonnaAlbumBrano.setCellValueFactory(cellData-> newjavafx.beans.property.SimpleStringProperty(ottieniTitoloAlbum(cellData.getValue().getAlbumBrano())));
  }
  private void caricaArtisti() {
    try {
      tuttiGliArtisti=artistaService.findAllArtisti();
      tabellaArtisti=setItems(FXCollections.observableArrayList(tuttiGliArtisti));
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli artisti: " + e.getMessage()); }
  }
  private void caricaAlbum() {
    try {
      tuttiGliAlbum=albumService.findAllAlbum();
      tabellaAlbum=setItems(FXCollections.observableArrayList(tuttiGliAlbum));
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli album: " + e.getMessage()); }
  }
  private void caricaBrani() {
    try {
      tuttiIbrani=branoService.findAllBrani();
      tuttiGliAlbum=albumService.findAllAlbum();
      for (Brano brano:tuttiIBrani) {
        brano.setAlbumBrano(null);
        for (Album album:tuttiGliAlbum) {
          for (Brano branoAlbum:album.getListaBrani()) {
            if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
              brano.setAlbumBrano(album);
              break;
            }
          }
          if (brano.getAlbumBrano()!=null) break;
        }
      }
      tabellaBrani.setItems(FXCollections.observableArrayList(tuttiIBrani));
    } catch (BusinessException e) {System.out.println("Errore nel caricamento dei brani: " + e.getMessage()); }
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista==null) return "";
    if (artista instanceod ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    else if ((artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "";
  }
  private String ottieniGenereArtista(Artista artista) {
    if (artista!=null&&artista.getGenerePrincipale()!=null) return artista.getGenerePrincipale().toString();
    return "";
  }
  private String ottieniTipoArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) return "SOLISTA";
    else if (artista instanceof ArtistaGruppo) return "GRUPPO";
    return "";
  }
  private String ottieniTitoloAlbum(Album album) {
    if (album==null) return "";
    return album.getTitolo();
  }
  private void configuraSelezioneArtista() {
    tabellaArtisti.getSelectionModel().selectedItemProperty.addListener((observable, vecchioArtista, nuovoArtista) -> {
      if (nuovoArtista==null) {
        tabellaAlbum.getItems().clear();
        tabellaBrani.getItems().clear();
        return;
      }
      caricaAlbumArtista(nuovoArtista); });
  }
  private void configuraSelezioneAlbum() {
    tabellaAlbum.getSelectionModel().selectedItemProperty.addListener((observable, vecchioAlbum, nuovoAlbum) -> {
      if (nuovoAlbum==null) {
        tabellaBrani.getItems().clear();
        return;
      }
      caricaBraniAlbum(nuovoAlbum); });
  }
  private void caricaBraniAlbum(Album album) {
    if (album==null) {
      tabellaBrani.setItems(FXCollections.observableArrayList());
      return;
    }
    List<Brano> braniAlbum=album.getListaBrani();
    if (braniAlbum==null) braniAlbum=new ArrayList<>();
    for (Brano brano:braniAlbum) brano.setAlbumBrano(album);
    tabellaBrani.setItems(FXCollections.observableArrayList(braniAlbum));
  }
  private void caricaAlbumArtista(Artista artista) {
    try {
      List<Album> tuttiGliAlbum=albumService.findAllAlbum();
      List<Album> albumArtista=new ArrayList<>();
      for (Album album: tuttiGliAlbum) {
        Artista artistaAlbum=album.getArtista();
        if (artistaAlbum!=null&&ottieniTipoArtista(artistaAlbum).equals(ottieniTipoArtista(artista))&&ottieniNomeArtista(artistaAlbum).equalsIgnoreCase(ottieniNomeArtista(artista))) 
          albumArtista.add(album);
      }
      tabellaAlbum.setItems(FXCollections.observableArrayList(albumArtista));
      tabellaBrani.getItems().clear();
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli album dell'artista: " + e.getMessage());}
  }
  private void configuraFiltro() {
    campoFiltro.textProperty().addListener((observable, vecchioTesto, nuovoTesto)->{applicaFiltro(nuovoTesto);});
  }
  private void applicaFiltro(String testo) {
    if (testo==null) return;
    String filtro=testo.trim().toLowerCase();
    List<Artista> artistiFiltrati=new ArrayList<>();
    List<Album> albumFiltrati=new ArrayList<>();
    List<Brano> braniFiltrati=new ArrayList<>();
    if (filtro.isEmpty()) {
      tabellaArtisti.setItems(FXCollections.observableArrayList(tuttiGliArtisti));
      tabellaAlbum.setItems(FXCollections.observableArrayList(tuttiGliAlbum));
      tabellaBrani.setItems(FXCollections.observableArrayList(tuttiIBrani));
      return;
    }
    for (Artista artista:tuttiGliArtisti) {
      String nome=ottieniNomeArtista(artista);
      if(nome!=null&&nome.toLowerCase().contains(filtro)) {
        artistiFiltrati.add(artista);
        for (Album album:tuttiGliAlbum) {
          if (album.getArtista()!=null&&ottieniNomeArtista(album.getArtista()).equalsIgnoreCase(nome)) {
            albumFiltrati.add(album);
            for(Brano brano:tuttiIBrani) {
              for (Brano branoAlbum: album.getListaBrani()) {
                if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
                  if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
                  break;
                }
              }
            }
          }
        }
      }
    }
    for (Album album:tuttiGliAlbum) {
      String titolo=album.getTitolo();
      if(titolo!=null&&titolo.toLowerCase().comtains(filtro)) {
        if(!albumFiltrati.contains(album)) albumFiltrati.add(album);
        if (album.getArtista()!=null&&!artistiFiltrati.contains(album.getArtista())) artistiFiltrati.add(album.getArtista());
        for (Brano branoAlbum: album.getListaBrani()) {
          for (Brano brano:tuttiIBrani) {
            if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
              if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
              break;
            }
          }
        }
      }
    }
    for (Brano brano:tuttiIBrani) {
      String titolo=brano.getTitolo();
      if (titolo!=null&&titolo.toLowerCase().contains(filtro)) {
        if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
        for (Album album:tuttiGliAlbum) {
          for (Brano branoAlbum: album.getListaBrani()) {
            if (branoAlbum.getTitolo()!=null&&titolo.equalsIgnoreCase(branoAlbum.getTitolo())) {
              if (!albumFiltrati.contains(album)) alcumFiltrati.add(album);
              if (album.getArtista()!=null&&!artistiFiltrati.contains(album.getArtista())) artistiFiltrati.add(album.getArtista());
              break;
            }
          }
        }
      }
    }
    tabellaArtisti.setItems(FXCollections.observableArrayList(artistiFiltrati));
    tabellaAlbum.setItems(FXCollections.observableArrayList(albumFiltrati));
    tabellaBrani.setItems(FXCollections.observableArrayList(braniFiltrati));
  }
  @FXML
  private void tornaIndidetro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
}
