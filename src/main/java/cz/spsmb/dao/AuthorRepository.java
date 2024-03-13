package cz.spsmb.dao;

import cz.spsmb.model.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;


@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {
    public Optional<Author> listByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public Author listById(Long id) {
        return findById(id);
    }

}