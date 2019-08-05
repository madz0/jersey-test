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
        AppConfig.registerExceptionMappersPackage(resourceConfig);
        return resourceConfig;
    }

    @Test
    public void request_whenPathIsNotExists_thenNotFound() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(null);
        Response response = target("/wrong/path").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void request_whenMethodIsWrong_thenMethodNotAllowed() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(null);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.METHOD_NOT_ALLOWED.getStatusCode(),
                Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void create_whenJsonDataIsMalformedRequest_thenRespondBadRequest() {
        Account account = new Account();
        account.setCurrency(null);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("Hey I'm malformed"));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
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
    public void create_whenPostNegativeAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(new BigDecimal("-1"));
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void create_whenPostZeroAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(BigDecimal.ZERO);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
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
    public void create_whenCurrencyIsNotValidRequest_thenRespondBadRequest() {
        Account account = new Account();
        account.setCurrency(null);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"amount\":\"10\", \"currency\":\"INVALID_CURRENCY\"}"));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void update_whenPostNullAmount_thenBadRequest() {
        Account account1 = new Account();
        account1.setAmount(null);
        account1.setCurrency(Currency.USD);
        account1.setVersion(1L);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for money"));
    }

    @Test
    public void update_whenPostNullVersion_thenBadRequest() {
        Account account1 = new Account();
        account1.setAmount(BigDecimal.TEN);
        account1.setCurrency(Currency.USD);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("version is mandatory for update"));
    }

    @Test
    public void update_whenPostNegativeAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(new BigDecimal("-1"));
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void update_whenPostZeroAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(BigDecimal.ZERO);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void transferGet_whenAccountIdLessThanZero_thenBadRequest() {
        Response response = target(BASE_PATH + "/0/transfers/1").request().get();
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for accountId"));
    }

    @Test
    public void transferGet_whenTransferIdLessThanZero_thenBadRequest() {
        Response response = target(BASE_PATH + "/1/transfers/0").request().get();
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for transferId"));
    }

    @Test
    public void transferList_whenIdLessThanZero_thenBadRequest() {
        Response response = target(BASE_PATH + "/0/transfers").request().get();
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Wrong value for id"));
    }

    @Test
    public void update_whenPostTooBigAmount_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void update_whenPostTooBigAmountFraction_thenBadRequest() {
        Account account = new Account();
        account.setCurrency(Currency.USD);
        StringBuilder number = new StringBuilder();
        number.append("9.");
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY_FRACTION; i++) {
            number.append("9");
        }
        account.setAmount(new BigDecimal(number.toString()));
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Unsupported value for money"));
    }

    @Test
    public void transfer_whenAmountIsNull_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, null, null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Wrong value for money", json.contains("Wrong value for money"));
    }

    @Test
    public void transfer_whenFromIsNull_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(null, to, BigDecimal.TEN, null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Wrong value for from account", json.contains("Wrong value for from account"));
    }

    @Test
    public void transfer_whenToIsNull_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, null, BigDecimal.TEN, null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Wrong value for from account", json.contains("Wrong value for to account"));
    }

    @Test
    public void transfer_whenMoneyIsTooBig_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY; i++) {
            number.append("9");
        }
        Transfer transfer = new Transfer(from, to, new BigDecimal(number.toString()), null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Unsupported value for amount", json.contains("Unsupported value for amount"));
    }

    @Test
    public void transfer_whenMoneyFractionIsMuch_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        StringBuilder number = new StringBuilder();
        number.append("9.");
        for (int i = 0; i <= BaseModel.MAX_SUPPORTED_MONEY_FRACTION; i++) {
            number.append("9");
        }
        Transfer transfer = new Transfer(from, to, new BigDecimal(number.toString()), null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Unsupported value for amount", json.contains("Unsupported value for amount"));
    }

    @Test
    public void transfer_whenAmountIsZero_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, new BigDecimal("0"), null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Unsupported value for amount", json.contains("Unsupported value for amount"));
    }

    @Test
    public void transfer_whenAmountIsNegative_thenBadRequest() {
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, new BigDecimal("-0.1"), null, null, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("Response should contain Unsupported value for amount", json.contains("Unsupported value for amount"));
    }
}