package com.example.controller;
import java.util.ArrayDeque;
import java.util.Deque;
public class Navigazione { private static final Deque<Runnable> cronologia = new ArrayDeque<>();
    private static Runnable azioneHome;
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
            if (azioneHome != null) { azioneHome.run();
            } else { System.out.println("Home non ancora registrata nella navigazione."); }
        }
    }
    public static void svuotaCronologia() { cronologia.clear(); }
}
