package cz.spsmb.dao;

import cz.spsmb.model.Album;
import cz.spsmb.model.Author;
import cz.spsmb.model.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {
    public Optional<Song> findByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public List<Song> listByAuthor(Author author) {
        return find("author", author).list();
    }

    public List<Song> listByAlbum(Album album) {
        return find("album", album).list();
    }

    public Song listById(Long id) {
        return findById(id);
    }

}