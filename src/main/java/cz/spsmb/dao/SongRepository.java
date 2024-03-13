package cz.spsmb.dao;

import cz.spsmb.model.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;


@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {
    public Optional<Song> listByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public Song listById(Long id) {
        return findById(id);
    }

}