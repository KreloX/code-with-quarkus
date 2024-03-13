package cz.spsmb.rest;

import cz.spsmb.dao.AlbumRepository;
import cz.spsmb.dao.AuthorRepository;
import cz.spsmb.dto.AlbumDTO;
import cz.spsmb.model.Album;
import cz.spsmb.model.Author;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/albums")
public class AlbumResource {

    @Inject
    AuthorRepository authorRepository;

    @Inject
    AlbumRepository albumRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response list() {
        List<Album> albums = albumRepository.listAll();
        return Response.ok().entity(albums.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        Album album = albumRepository.findById(id);
        return Response.ok().entity(album.toString()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        albumRepository.deleteById(id);
        return Response.ok().entity("ok").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response save(AlbumDTO albumDTO) {
        String name = albumDTO.getName();
        String authorName = albumDTO.getAuthor();
        if (name == null || authorName == null) {
            return Response.status(400).entity("Author must have attributes \"name\" and \"author\".").build();
        }

        Album album = new Album();

        Author author = authorRepository.listByName(authorName).orElseGet(() -> {
            Author newAuthor = new Author(authorName);
            newAuthor.getAlbums().add(album);
            authorRepository.persist(newAuthor);
            return newAuthor;
        });

        album.setName(name);
        album.setAuthor(author);
        albumRepository.persist(album);
        return Response.ok().entity("ok").build();
    }
}
