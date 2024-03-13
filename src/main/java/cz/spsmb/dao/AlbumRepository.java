package cz.spsmb.dao;

import cz.spsmb.model.Album;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;


@ApplicationScoped
public class AlbumRepository implements PanacheRepository<Album> {
    public Optional<Album> listByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public Album listById(Long id) {
        return findById(id);
    }

}