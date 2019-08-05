package com.github.madz0.revolut.resource;

import com.github.madz0.revolut.model.Account;
import com.github.madz0.revolut.model.Currency;
import com.github.madz0.revolut.model.Transfer;
import com.github.madz0.revolut.repository.Page;
import com.github.madz0.revolut.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.github.madz0.revolut.resource.AccountsResource.BASE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountsResourcePresentationIntegrationTest extends JerseyTest {
    @Mock
    AccountService accountService;

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        //return new AppConfig(new Properties());
        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(new AccountsResource(accountService));
    }

    @Test
    public void list_whenProperRequest_thenOkAndMoneyShouldBeRounded() {
        Account account1 = new Account();
        account1.setAmount(new BigDecimal("1.0001"));
        account1.setCurrency(Currency.USD);
        account1.setId(1L);
        Account account2 = new Account();
        account2.setAmount(new BigDecimal("2.0002"));
        account2.setCurrency(Currency.EUR);
        account2.setId(2L);
        Account account3 = new Account();
        account3.setAmount(new BigDecimal("3.9999"));
        account3.setCurrency(Currency.EUR);
        account3.setId(3L);
        Account account4 = new Account();
        account4.setAmount(new BigDecimal("5.55"));
        account4.setCurrency(Currency.EUR);
        account4.setId(4L);
        List<Account> accounts = Arrays.asList(account1, account2, account3, account4);
        setupForSuccessfulFindAll(accounts);
        int page = 0;
        int pageSize = 10;
        Response response = target(BASE_PATH)
                .queryParam("page", page)
                .queryParam("pageSize", pageSize).request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("TotalSize should be " + accounts.size() + ", page should be " + page + ", pageSize " + pageSize, json.contains("\"totalSize\":" + accounts.size() + ",\"page\":" + page + ",\"pageSize\":" + pageSize));
        assertTrue("1.0001 should converted to 1.00", json.contains("1.00") && !json.contains("1.0001"));
        assertTrue("2.0002 should converted to 2.00", json.contains("2.00") && !json.contains("2.0002"));
        assertTrue("3.9999 should converted to 4.00", json.contains("4.00") && !json.contains("3.9999"));
        assertTrue("5.55 should remain intact", json.contains("5.55"));
    }

    @Test
    public void get_whenProperRequest_thenOkAndMoneyShouldBeRounded() {
        Account account1 = new Account();
        account1.setAmount(new BigDecimal("1.0001"));
        account1.setCurrency(Currency.USD);
        account1.setId(1L);
        setupForSuccessfulGet(account1);
        Response response = target(BASE_PATH + "/1").request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("1.0001 should converted to 1.00", json.contains("1.00") && !json.contains("1.0001"));
    }

    @Test
    public void getTransfer_whenProperRequest_thenOkAndMoneyShouldBeRounded() {

        Account account1 = new Account();
        account1.setAmount(BigDecimal.TEN);
        account1.setCurrency(Currency.USD);
        account1.setId(1L);
        Account account2 = new Account();
        account2.setAmount(BigDecimal.TEN);
        account2.setCurrency(Currency.EUR);
        account2.setId(2L);
        Transfer transfer1 = new Transfer(account1, account2, new BigDecimal("1.4123"), account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
        transfer1.setId(1L);
        transfer1.setVersion(0L);
        setupForSuccessfulTransferGet(transfer1);
        Response response = target(BASE_PATH + "/1/transfers/1").request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("1.4123 should converted to 1.00", json.contains("1.41") && !json.contains("1.4123"));
    }

    @Test
    public void transferList_whenProperRequest_thenOkAndMoneyShouldBeRounded() {
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

        Transfer transfer1 = new Transfer(account1, account2, new BigDecimal("1.0001"), account1.getCurrency(), account2.getCurrency(), BigDecimal.ONE);
        transfer1.setId(1L);
        Transfer transfer2 = new Transfer(account1, account3, new BigDecimal("2.0002"), account1.getCurrency(), account3.getCurrency(), BigDecimal.ONE);
        transfer2.setId(2L);
        Transfer transfer3 = new Transfer(account3, account1, new BigDecimal("3.9999"), account3.getCurrency(), account1.getCurrency(), BigDecimal.ONE.add(BigDecimal.TEN));
        transfer3.setId(3L);
        Transfer transfer4 = new Transfer(account2, account1, new BigDecimal("5.55"), account3.getCurrency(), account1.getCurrency(), BigDecimal.ONE.add(BigDecimal.TEN));
        transfer4.setId(4L);
        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3, transfer4);
        setupForSuccessfulTransferFindAll(transfers);
        int page = 0;
        int pageSize = 10;
        Response response = target(BASE_PATH + "/1/transfers")
                .queryParam("page", page)
                .queryParam("pageSize", pageSize).request().get();
        assertEquals("Http Response should be " + Response.Status.OK.getStatusCode(),
                Response.Status.OK.getStatusCode(), response.getStatus());
        String json = response.readEntity(String.class);
        assertTrue("TotalSize should be " + transfers.size() + ", page should be 0, pageSize 10", json.contains("\"totalSize\":" + transfers.size() + ",\"page\":" + page + ",\"pageSize\":" + pageSize));
        assertTrue("1.0001 should converted to 1.00", json.contains("1.00") && !json.contains("1.0001"));
        assertTrue("2.0002 should converted to 2.00", json.contains("2.00") && !json.contains("2.0002"));
        assertTrue("3.9999 should converted to 4.00", json.contains("4.00") && !json.contains("3.9999"));
        assertTrue("5.55 should remain intact", json.contains("5.55"));
    }

    private void setupForSuccessfulFindAll(final List<Account> accountList) {
        doAnswer(invocationOnMock -> {
            accountList.forEach(invocationOnMock.getArgument(2));
            return new Page<>(accountList, accountList.size(), invocationOnMock.getArgument(0), invocationOnMock.getArgument(1));
        }).when(accountService).findAll(anyInt(), anyInt(), any());
    }

    private void setupForSuccessfulGet(Account account) {
        doAnswer(invocationOnMock -> account).when(accountService).findById(anyLong());
    }

    private void setupForSuccessfulTransferGet(Transfer transfer) {
        doAnswer(invocationOnMock -> transfer).when(accountService).findTransferById(anyLong(), anyLong());
    }

    private void setupForSuccessfulTransferFindAll(List<Transfer> transfers) {
        doAnswer(invocationOnMock -> {
            transfers.forEach(invocationOnMock.getArgument(3));
            return new Page<>(transfers, transfers.size(), invocationOnMock.getArgument(1), invocationOnMock.getArgument(2));
        }).when(accountService).findAllTransfersByAccountId(anyLong(), anyInt(), anyInt(), any());
    }
}