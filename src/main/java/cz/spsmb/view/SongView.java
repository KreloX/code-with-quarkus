package cz.spsmb.view;

import cz.spsmb.dao.AlbumRepository;
import cz.spsmb.dao.AuthorRepository;
import cz.spsmb.dao.SongRepository;
import cz.spsmb.dto.SongDTO;
import cz.spsmb.model.Album;
import cz.spsmb.model.Author;
import cz.spsmb.model.Song;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.util.List;

@Named
@RequestScoped
public class SongView {
    @Inject
    SongRepository songRepository;
    @Inject
    AuthorRepository authorRepository;
    @Inject
    AlbumRepository albumRepository;

    SongDTO songDTO;

    List<Song> songs;
    List<Author> authors;

    @PostConstruct
    public void init() {
        songDTO = new SongDTO();

        songs = songRepository.listAll();
        authors = authorRepository.listAll();
    }

    public void filterByAuthor(Author author) {
        songs = songRepository.listByAuthor(author);
    }

    public void filterByAlbum(Album album) {
        songs = songRepository.listByAlbum(album);
    }

    public List<Album> albumsByAuthor(Author author) {
        return albumRepository.listByAuthor(author);
    }

    @Transactional
    public void saveSong() {
        String name = songDTO.getName();
        String authorName = songDTO.getAuthor();
        String albumName = songDTO.getAlbum();
        if (name == null || authorName == null || albumName == null) return;
        if (name.isBlank() || authorName.isBlank() || albumName.isBlank()) return;

        Song song = new Song(name.trim());

        Author author = authorRepository.listByName(authorName.trim()).orElseGet(() -> {
            Author newAuthor = new Author(authorName.trim());
            newAuthor.getSongs().add(song);
            authorRepository.persist(newAuthor);
            return newAuthor;
        });

        String trimmedAlbumName = albumName.trim() + " (" + authorName.trim() + ")";
        Album album = albumRepository.listByName(trimmedAlbumName).orElseGet(() -> {
            Album newAlbum = new Album(trimmedAlbumName, author);
            newAlbum.getSongs().add(song);
            albumRepository.persist(newAlbum);
            author.getAlbums().add(newAlbum);
            authorRepository.persist(author);
            return newAlbum;
        });

        song.setAuthor(author);
        song.setAlbum(album);
        songRepository.persist(song);

        init();
    }

    public SongDTO getSongDTO() {
        return songDTO;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Author> getAuthors() {
        return authors;
    }
}
