package com.github.madz0.jerseytest.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.madz0.jerseytest.config.AppConfig;
import com.github.madz0.jerseytest.exception.*;
import com.github.madz0.jerseytest.model.Account;
import com.github.madz0.jerseytest.model.Currency;
import com.github.madz0.jerseytest.model.Transfer;
import com.github.madz0.jerseytest.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.github.madz0.jerseytest.AbstractUnitTest.TestUtils.createProperTransferJson;
import static com.github.madz0.jerseytest.resource.AccountsResource.BASE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountsResourceExceptionsResponseIntegrationTest extends JerseyTest {
    @Mock
    AccountService accountService;

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        MockitoAnnotations.initMocks(this);
        ResourceConfig resourceConfig = new ResourceConfig().register(new AccountsResource(accountService));
        AppConfig.registerExceptionMappersPackage(resourceConfig);
        return resourceConfig;
    }

    @Test
    public void create_whenThrowsRestIllegalArgException_thenBadRequest() {
        setupForRestIllegalArgCreate();
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void list_whenThrowsRestUnsupportedException_thenBadRequest() {
        setupForRestUnsupportedExceptionList();
        Response response = target(BASE_PATH)
                .queryParam("page", 0)
                .queryParam("pageSize", 10).request().get();
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void get_whenDataIntegrationException_thenNotFound() {
        dataIntegrityExceptionGet();
        Response response = target(BASE_PATH + "/1").request().get();
        assertEquals("Http Response should be " + Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void update_ConcurrentModificationEx_thenConflict() {
        setupForConcurrentModificationExUpdate();
        Account account1 = new Account();
        account1.setAmount(BigDecimal.TEN);
        account1.setCurrency(Currency.USD);
        account1.setId(1L);
        account1.setVersion(1L);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.CONFLICT.getStatusCode(),
                Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void transfer_whenDbQueryException_thenInternalServerErrorAndContainsErrorCode() throws JsonProcessingException {
        setupForDbQueryExceptionTransfer();
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, null, null, null);

        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createProperTransferJson(transfer)));
        assertEquals("Http Response should be " + Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain error code", json.toLowerCase().contains("code"));
    }

    @Test
    public void getTransfer_whenDataIntegrationException_thenNotFound() {
        dataIntegrityExceptionGetTransfer();
        Response response = target(BASE_PATH + "/1/transfers/1").request().get();
        assertEquals("Http Response should be " + Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void transfer_whenUncaughtException_thenInternalServerErrorAndContainsErrorCode() throws JsonProcessingException {
        setupForUnCaughtExceptionTransfer();
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createProperTransferJson(transfer)));
        assertEquals("Http Response should be " + Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain error code", json.toLowerCase().contains("code"));
    }

    private void setupForRestIllegalArgCreate() {
        doThrow(RestIllegalArgumentException.class).when(accountService).create(any(Account.class));
    }

    private void setupForRestUnsupportedExceptionList() {
        doThrow(RestUnsupportedOperationException.class).when(accountService).findAll(anyInt(), anyInt());
        doThrow(RestUnsupportedOperationException.class).when(accountService).findAll(anyInt(), anyInt(), any());
    }

    private void dataIntegrityExceptionGet() {
        doThrow(DataIntegrityException.class).when(accountService).findById(anyLong());
    }

    private void dataIntegrityExceptionGetTransfer() {
        doThrow(DataIntegrityException.class).when(accountService).findTransferById(anyLong(), anyLong());
    }

    private void setupForConcurrentModificationExUpdate() {
        doThrow(RestConcurrentModificationException.class).when(accountService).update(any(Account.class));
    }

    private void setupForDbQueryExceptionTransfer() {
        doThrow(DbQueryException.class).when(accountService).makeTransfer(any(Transfer.class));
    }

    private void setupForUnCaughtExceptionTransfer() {
        doThrow(ArithmeticException.class).when(accountService).makeTransfer(any(Transfer.class));
    }
}