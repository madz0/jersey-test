package com.github.madz0.revolut.resource;

import com.github.madz0.revolut.config.AppConfig;
import com.github.madz0.revolut.exception.*;
import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.BaseModel;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.service.AccountService;
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

import static com.github.madz0.revolut.resource.AccountsResource.BASE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountsResourceValidationResponseIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        ResourceConfig resourceConfig = new ResourceConfig().register(new AccountsResource(null));
        AppConfig.registerBeanValidationResponse(resourceConfig);
        return resourceConfig;
    }

    @Test
    public void create_whenPostNullAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(null);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for money"));
    }

    @Test
    public void create_whenPostNullCurrency_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(null);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for currency"));
    }

    @Test
    public void create_whenPostTooBigAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void create_whenPostTooBigAmountFraction_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        StringBuilder number = new StringBuilder();
        number.append("9.");
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY_FRACTION; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void list_whenThrowsRestUnsupportedException_thenBadRequest() {
        Response response = target(BASE_PATH)
                .queryParam("page", 0)
                .queryParam("pageSize", 10).request().get();
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void get_whenDataIntegrationException_thenNotFound() {
        Response response = target(BASE_PATH + "/1").request().get();
        assertEquals("Http Response should be " + Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void update_ConcurrentModificationEx_thenConflict() {
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
    public void transfer_whenDbQueryException_thenInternalServerErrorAndContainsErrorCode() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain error code", json.toLowerCase().contains("code"));
    }

    @Test
    public void transfer_whenUncaughtException_thenInternalServerErrorAndContainsErrorCode() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain error code", json.toLowerCase().contains("code"));
    }
}