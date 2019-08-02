package com.github.madz0.revolut.resource;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountsResourceIntegrationTest extends JerseyTest {

    private final static String BASE_PATH = "/api/accounts";
    @Override
    protected Application configure() {
        return new ResourceConfig(AccountsResource.class);
    }

    @Test
    public void create_whenCorrectRequest_thenCreatedAndContainsLocationHeader() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be 201 ", Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue("Http Response header should contains created location", response.getHeaderString("Location").contains(BASE_PATH));
    }

    @Test
    public void list() {
    }

    @Test
    public void get() {
    }

    @Test
    public void update() {
    }

    @Test
    public void transfer() {
    }
}