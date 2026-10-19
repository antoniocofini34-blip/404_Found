package com.example.controller;
import java.util.ArrayDeque; //serve per avere una struttura dati (pile, code) ad alte prestazioni
import java.util.Deque; //serve per avere un'interfaccia per gestire in modo flessibile strutture dati FIFO e LIFO
public class Navigazione { private static final Deque<Runnable> cronologia = new ArrayDeque<>();
    private static Runnable azioneHome; //azione eseguibile per la schermata principale
    private Navigazione() {}
    public static void impostaHome(Runnable azione) { azioneHome = azione; }
    public static void salvaSchermataPrecedente(Runnable azioneIndietro) {
        if (azioneIndietro != null) { cronologia.push(azioneIndietro); }
    }
    public static boolean indietro() {
        if (cronologia.isEmpty()) { return false; }
        Runnable azione = cronologia.pop();
        azione.run();
        return true;
    }
    public static void indietroOHome() {
        if (!indietro()) {
            if (azioneHome != null) { azioneHome.run(); }
        }
    }
    public static void svuotaCronologia() { cronologia.clear(); }
}
