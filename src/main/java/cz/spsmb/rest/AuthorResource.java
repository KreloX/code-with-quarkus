package cz.spsmb.rest;

import cz.spsmb.dao.AuthorRepository;
import cz.spsmb.model.Author;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/authors")
public class AuthorResource {

    @Inject
    AuthorRepository authorRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response list() {
        List<Author> authors = authorRepository.listAll();
        return Response.ok().entity(authors.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        Author author = authorRepository.findById(id);
        return Response.ok().entity(author.toString()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        authorRepository.deleteById(id);
        return Response.ok().entity("ok").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response save(Author author) {
        if (author.getName() == null) Response.status(400).entity("Author must have attribute \"name\".").build();

        authorRepository.persist(author);
        return Response.ok().entity("ok").build();
    }
}
