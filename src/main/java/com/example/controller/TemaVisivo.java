package com.example.controller;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
public final class TemaVisivo {
  public enum Tema { CHIARO, SCURO }
  private static Tema temaCorrente=Tema.SCURO;
  private TemaVisivo() { }
  public static boolean isScuro() { return temaCorrente==Tema.SCURO; }
  public static void impostaTema(Tema tema) { 
    if (tema!=null) temaCorrente=tema;
  }
  public static String getColoreBarra() { return "white"; }
  public static String getColoreSfondo() { return isScuro() ? "#121212" : "#f5f5f5"; }
  public static String getColoreTesto() { return isScuro() ? "white" : "#111111"; }
  public static String getColoreSecondario() { return isScuro() ? "#bbbbbb" : "#555555"; }
  public static void applicaTema(Node nodo) {
    if (nodo==null) return;
    String testo=getColoreTesto();
    String sfondo=getColoreSfondo();
    String secondario=getColoreSecondario();
    if (nodo instanceof Label) {
      Label label=(Label) nodo;
      label.setTextFill(Color.web(testo));
    }
    if (nodo instanceof TextField) {
      TextField campo=(TextField) nodo;
      String sfondoCampo=isScuro() ? "#242424" : "#ffffff";
      String bordoCampo=isScuro() ? "#454545" : "#cccccc";
      campo.setStyle("-fx-control-inner-background: " + sfondoCampo + ";" + "-fx-background-color: " + sfondoCampo + ";" + "-fx-text-fill: " + testo + ";" + "-fx-prompt-text-fill: " + secondario + ";" + "-fx-border-color: " + bordoCampo + ";");
    }
    if (nodo instanceof DatePicker) {
      DatePicker picker=(DatePicker) nodo;
      String sfondoCampo=isScuro() ? "#242424" : "#ffffff";
      String bordoCampo=isScuro() ? "#454545" : "#cccccc";
      picker.setStyle("-fx-background-color: " + sfondoCampo + ";" + "-fx-control-inner-background: " + sfondoCampo + ";" + "-fx-text-fill: " + testo + ";" + "-fx-prompt-text-fill: " + secondario + ";" + "-fx-border-color: " + bordoCampo + ";");
      picker.applyCss();
      Node editor=picker.lookup(".text-field");
      editor.setStyle("-fx-control-inner-background: " + sfondoCampo + ";" + "-fx-background-color: " + sfondoCampo + ";" + "-fx-text-fill: " + testo + ";" + "-fx-prompt-text-fill: " + secondario + ";" + "-fx-border-color: transparent;");
      Node pulsanteCalendario=picker.lookup(".arrow-button");
      pulsanteCalendario.setStyle("-fx-background-color: " + sfondoCampo + ";");
    }
    if (nodo instanceof TextArea) {
      TextArea area=(TextArea) nodo;
      String sfondoCampo=isScuro() ? "#242424" : "#ffffff";
      String bordoCampo=isScuro() ? "#454545" : "#cccccc";
      area.setStyle("-fx-control-inner-background: " + sfondoCampo + ";" + "-fx-background-color: " + sfondoCampo + ";" + "-fx-text-fill: " + testo + ";" + "-fx-prompt-text-fill: " + secondario + ";" + "-fx-border-color: " + bordoCampo + ";");
      area.applyCss();
      Node contenuto=area.lookup(".content");
      if (contenuto!=null) contenuto.setStyle("-fx-background-color: " + sfondoCampo + ";");
      Node viewport=area.lookup(".viewport");
      if (viewport!=null) viewport.setStyle("-fx-background-color: " + sfondoCampo + ";");
    }
    if (nodo instanceof TableView<?>) {
      TableView<?> tabella=(TableView<?>) nodo;
      String coloreBordo=isScuro() ? "#383838" : "#d0d0d0";
      String sfondoCella=isScuro() ? "#242424" : "#ffffff";
      tabella.setStyle("-fx-base: " + sfondo + ";" + "-fx-background: " + sfondo + ";" + "-fx-control-inner-background: " + sfondoCella + ";" + "-fx-table-cell-border-color: " + coloreBordo + ";");
      tabella.applyCss();
      for (Node intestazione : tabella.lookupAll(".column-header.label")) {
        if (intestazione instanceof Label) ((Label) intestazione).setTextFill(Color.web(testo));
      }
      for (Node cella : tabella.lookupAll(".table-cell")) {
        if (cella instanceof javafx.scene.control.TableCell<?,?>) ((javafx.scene.control.TableCell<?,?>) cella).setTextFill(Color.web(testo));
      }
    }
    if (nodo instanceof Slider) applicaStileSlider((Slider)nodo);
    if (nodo instanceof Parent) {
      Parent parent=(Parent) nodo;
      for (Node figlio : parent.getChildrenUnmodifiable()) { applicaTema(figlio); }
    }
  }
  public static void applicaStileSlider(Slider slider) {
    if (slider==null) return;
    String colore=getColoreBarra();
    slider.setStyle("-fx-control-inner-background: " + colore + ";");
    slider.applyCss();
    Node track=slider.lookup(".track");
    if (track!=null) track.setStyle("-fx-background-color: #808080;" + "-fx-background-radius: 5;");
    Node thumb=slider.lookup(".thumb");
    if (thumb!=null) thumb.setStyle("-fx-background-color: " + colore + ";" + "-fx-background-radius: 10;");
  }
}
