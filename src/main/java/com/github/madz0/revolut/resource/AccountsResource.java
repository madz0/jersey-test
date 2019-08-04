package com.github.madz0.revolut.resource;

import com.github.madz0.revolut.exception.RestIllegalArgumentException;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.service.AccountService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path(AccountsResource.BASE_PATH)
public class AccountsResource {
    final static String BASE_PATH = "/api/accounts";
    private final AccountService accountService;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid @ConvertGroup(to = Account.Create.class) Account account) throws URISyntaxException {
        return Response.created(new URI(BASE_PATH + "/" + accountService.create(account).getId())).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
        return Response.ok(accountService.findAll(page, pageSize, account -> account.setRoundMoney(true))).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        Account account = accountService.findById(id);
        account.setRoundMoney(true);
        return Response.ok(account).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid @ConvertGroup(to = Account.Update.class) Account account) {
        if (id == null || id <= 0) {
            throw new RestIllegalArgumentException("invalid id");
        }
        account.setId(id);
        accountService.update(account);
        return Response.ok().build();
    }

    @POST
    @Path("/transfers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid Transfer transfer) throws URISyntaxException {
        return Response.created(new URI(BASE_PATH + "/" + transfer.getFromAccountId() + "/transfers/" + accountService.makeTransfer(transfer).getId())).build();
    }
}
