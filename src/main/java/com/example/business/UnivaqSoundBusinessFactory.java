package com.example.business;
public interface UnivaqSoundBusinessFactory {
    ArtistaService getArtistaService();
    AlbumService getAlbumService();
    BranoService getBranoService();
    CatalogoService getCatalogoService();
    CodaRiproduzioneService getCodaRiproduzioneService();
    CronologiaAscoltiService getCronologiaAscoltiService();
    PlaylistService getPlaylistService();
    RicercaService getRicercaService();
    UtenteService getUtenteService();
}
