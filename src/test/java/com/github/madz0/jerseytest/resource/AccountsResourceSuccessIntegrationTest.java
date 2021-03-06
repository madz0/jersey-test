package com.github.madz0.jerseytest.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.madz0.jerseytest.model.Account;
import com.github.madz0.jerseytest.model.Currency;
import com.github.madz0.jerseytest.model.Transfer;
import com.github.madz0.jerseytest.repository.Page;
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
import java.util.Arrays;
import java.util.List;

import static com.github.madz0.jerseytest.AbstractUnitTest.TestUtils.createProperTransferJson;
import static com.github.madz0.jerseytest.resource.AccountsResource.BASE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountsResourceSuccessIntegrationTest extends JerseyTest {
    @Mock
    AccountService accountService;

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");

        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(new AccountsResource(accountService));
    }

    @Test
    public void create_whenCorrectRequest_thenCreatedAndContainsLocationHeader() {
        setupForSuccessfulCreate();
        Account account = new Account();
        account.setCurrency(Currency.USD);
        account.setAmount(BigDecimal.TEN);
        Response response = target(BASE_PATH).request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http Response should be " + Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue("Http Response header should contains created location", response.getHeaderString("Location").contains(BASE_PATH));
    }

    @Test
    public void list_whenProperRequest_thenOkAndContainsPageData() {
        setupForSuccessfulFindAll();
        Response response = target(BASE_PATH)
                .queryParam("page", 0)
                .queryParam("pageSize", 10).request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("TotalSize should be 3, page should be 0, pageSize 10", json.contains("\"totalSize\":3,\"page\":0,\"pageSize\":10"));
        assertTrue("There should be contents", json.contains("{\"contents\":[{"));
    }

    @Test
    public void get_whenProperRequest_thenOkAndContainsResourceData() {
        setupForSuccessfulGet();
        Response response = target(BASE_PATH + "/1").request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("There should be id and version in the response", json.contains("{\"id\":1,\"version\":"));
    }

    @Test
    public void update_whenProperRequest_thenOk() {
        setupForSuccessfulUpdate();
        Account account1 = new Account();
        account1.setAmount(BigDecimal.TEN);
        account1.setCurrency(Currency.USD);
        account1.setId(1L);
        account1.setVersion(1L);
        Response response = target(BASE_PATH + "/1").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals("Http response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void transfer() throws JsonProcessingException {
        setupForSuccessfulTransfer();
        Account from = new Account();
        from.setId(1L);
        Account to = new Account();
        to.setId(2L);
        Transfer transfer = new Transfer(from, to, BigDecimal.TEN, Currency.USD, Currency.EUR, null);
        Response response = target(BASE_PATH + "/transfers").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createProperTransferJson(transfer)));
        assertEquals("Http Response should be " + Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue("Http Response header should contains created location", response.getHeaderString("Location").contains(BASE_PATH + "/" + from.getId() + "/transfers"));
    }

    @Test
    public void getTransfer_whenProperRequest_thenOkAndContainsResourceData() {
        setupForSuccessfulTransferGet();
        Response response = target(BASE_PATH + "/1/transfers/1").request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("There should be id and version in the response", json.contains("{\"id\":1,\"version\":"));
    }

    @Test
    public void transferList_whenProperRequest_thenOkAndContainsPageData() {
        setupForSuccessfulTransferFindAll();
        Response response = target(BASE_PATH + "/1/transfers")
                .queryParam("page", 0)
                .queryParam("pageSize", 10).request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("TotalSize should be 3, page should be 0, pageSize 10", json.contains("\"totalSize\":3,\"page\":0,\"pageSize\":10"));
        assertTrue("There should be contents", json.contains("{\"contents\":[{"));
    }

    private void setupForSuccessfulCreate() {
        doAnswer(invocationOnMock -> {
            Account acc = invocationOnMock.getArgument(0);
            acc.setId(1L);
            return acc;
        }).when(accountService).create(any(Account.class));
    }

    private void setupForSuccessfulFindAll() {
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(1L);
            Account account2 = new Account();
            account2.setAmount(BigDecimal.TEN);
            account2.setCurrency(Currency.EUR);
            account2.setId(2L);
            Account account3 = new Account();
            account3.setAmount(BigDecimal.TEN);
            account3.setCurrency(Currency.EUR);
            account3.setId(3L);
            List<Account> accountList = Arrays.asList(account1, account2, account3);
            return new Page<>(accountList, 3, invocationOnMock.getArgument(0), invocationOnMock.getArgument(1));
        }).when(accountService).findAll(anyInt(), anyInt(), any());
    }

    private void setupForSuccessfulGet() {
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(1L);
            account1.setVersion(1L);
            return account1;
        }).when(accountService).findById(anyLong());
    }

    private void setupForSuccessfulUpdate() {
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(1L);
            return account1;
        }).when(accountService).update(any(Account.class));
    }

    private void setupForSuccessfulTransfer() {
        doAnswer(invocationOnMock -> {
            Transfer transfer = invocationOnMock.getArgument(0);
            transfer.setId(1L);
            transfer.setExchangeRate(BigDecimal.ONE);
            transfer.setFromCurrency(Currency.USD);
            transfer.setToCurrency(Currency.USD);
            return transfer;
        }).when(accountService).makeTransfer(any(Transfer.class));
    }

    private void setupForSuccessfulTransferGet() {
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(invocationOnMock.getArgument(0));
            Account account2 = new Account();
            account2.setAmount(BigDecimal.TEN);
            account2.setCurrency(Currency.EUR);
            account2.setId(2L);
            Transfer transfer1 = new Transfer(account1, account2, BigDecimal.TEN, account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
            transfer1.setId(1L);
            transfer1.setVersion(0L);
            return transfer1;
        }).when(accountService).findTransferById(anyLong(), anyLong());
    }

    private void setupForSuccessfulTransferFindAll() {
        doAnswer(invocationOnMock -> {
            Account account1 = new Account();
            account1.setAmount(BigDecimal.TEN);
            account1.setCurrency(Currency.USD);
            account1.setId(invocationOnMock.getArgument(0));
            Account account2 = new Account();
            account2.setAmount(BigDecimal.TEN);
            account2.setCurrency(Currency.EUR);
            account2.setId(2L);
            Account account3 = new Account();
            account3.setAmount(BigDecimal.TEN);
            account3.setCurrency(Currency.EUR);
            account3.setId(3L);

            Transfer transfer1 = new Transfer(account1, account2, BigDecimal.TEN, account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
            transfer1.setId(1L);
            Transfer transfer2 = new Transfer(account1, account3, BigDecimal.ONE, account1.getCurrency(), account3.getCurrency(), BigDecimal.ONE);
            transfer2.setId(2L);
            Transfer transfer3 = new Transfer(account3, account1, BigDecimal.ONE, account3.getCurrency(), account1.getCurrency(), BigDecimal.ONE.add(BigDecimal.TEN));
            transfer3.setId(3L);
            return new Page<>(Arrays.asList(transfer1, transfer2, transfer3), 3, invocationOnMock.getArgument(1), invocationOnMock.getArgument(2));
        }).when(accountService).findAllTransfersByAccountId(anyLong(), anyInt(), anyInt(), any());
    }
}