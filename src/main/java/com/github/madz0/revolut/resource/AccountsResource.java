package com.github.madz0.revolut.resource;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/api/accounts")
public class AccountsResource {

    private AccountService accountService;

    @Inject
    public AccountsResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Account account) throws URISyntaxException {
        return Response.created(new URI("http://localhost/api/accounts/" + accountService.save(account).getId())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
        return Response.ok(accountService.findAll(page, pageSize)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(Long id) {
        return Response.ok(accountService.findById(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Account account) {
        accountService.save(account);
        return Response.ok().build();
    }

    @POST
    @Path("/transfers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(Transfer transfer) throws URISyntaxException {
        return Response.created(new URI("http://localhost/api/accounts/" + transfer.getFromAccountId() + "/transfers/" + accountService.makeTransfer(transfer).getId())).build();
    }
}
