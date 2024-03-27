package cz.spsmb.rest;

import cz.spsmb.dao.AlbumRepository;
import cz.spsmb.dao.AuthorRepository;
import cz.spsmb.dao.SongRepository;
import cz.spsmb.dto.SongDTO;
import cz.spsmb.model.Album;
import cz.spsmb.model.Author;
import cz.spsmb.model.Song;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/songs")
public class SongResource {

    @Inject
    SongRepository songRepository;

    @Inject
    AuthorRepository authorRepository;

    @Inject
    AlbumRepository albumRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response list() {
        List<Song> songs = songRepository.listAll();
        return Response.ok().entity(songs.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        Song song = songRepository.findById(id);
        return Response.ok().entity(song.toString()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        songRepository.deleteById(id);
        return Response.ok().entity("ok").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response save(SongDTO songDTO) {
        String name = songDTO.getName();
        String authorName = songDTO.getAuthor();
        String albumName = songDTO.getAlbum();
        if (name == null || authorName == null || albumName == null || name.isBlank() || authorName.isBlank() || albumName.isBlank()) {
            return Response.status(400).entity("Song must have attributes \"name\", \"author\" and \"album\".").build();
        }

        Song song = new Song(name.trim());

        Author author = authorRepository.listByName(authorName.trim()).orElseGet(() -> {
            Author newAuthor = new Author(authorName.trim());
            newAuthor.getSongs().add(song);
            authorRepository.persist(newAuthor);
            return newAuthor;
        });

        String trimmedAlbumName = albumName.trim() + " (" + authorName.trim() + ")";
        Album album = albumRepository.listByName(trimmedAlbumName.trim()).orElseGet(() -> {
            Album newAlbum = new Album(trimmedAlbumName.trim(), author);
            newAlbum.getSongs().add(song);
            albumRepository.persist(newAlbum);
            author.getAlbums().add(newAlbum);
            authorRepository.persist(author);
            return newAlbum;
        });

        song.setAuthor(author);
        song.setAlbum(album);
        songRepository.persist(song);
        return Response.ok().entity("ok").build();
    }
}
