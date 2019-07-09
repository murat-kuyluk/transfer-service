package com.mcorp.transfer.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mcorp.transfer.TestDataHelper;
import com.mcorp.transfer.dao.AccountDao;
import com.mcorp.transfer.dao.impl.AccountDaoImpl;
import com.mcorp.transfer.dao.entity.Account;
import com.mcorp.transfer.model.AccountDto;
import com.mcorp.transfer.model.TransferDto;
import com.mcorp.transfer.model.TransferRequest;
import com.mcorp.transfer.model.TransferResponse;
import com.mcorp.transfer.modules.DaoModule;
import com.mcorp.transfer.modules.PersistenceInitializer;
import com.mcorp.transfer.modules.PersistenceModule;
import com.mcorp.transfer.modules.ServiceModule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import spark.Spark;

import java.io.IOException;
import java.math.BigDecimal;

import static com.mcorp.transfer.model.AccountDto.AccountDtoBuilder.*;
import static org.assertj.core.api.Assertions.*;

public class ControllerStepDefinitions {

    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FROM_ACCOUNT_NUMBER = "00813796";
    private static final String TO_ACCOUNT_NUMBER = "70328725";
    private static final String FROM_SORT_CODE = "800551";
    private static final String TO_SORT_CODE = "080054";
    public static final String TRANSFER_SVC_URL = "http://localhost:4567/transfers";


    private AccountDao accountDao;
    private CloseableHttpResponse response;
    private Gson gson = new GsonBuilder().create();

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new PersistenceModule(), new ServiceModule(), new DaoModule());
        Spark.init();
        Spark.awaitInitialization();
        injector.getInstance(TransferController.class).initializeRoutes();
        injector.getInstance(PersistenceInitializer.class).startPersistService();
        accountDao = injector.getInstance(AccountDaoImpl.class);
    }

    @After
    public static void tearDown() {
        Spark.stop();
    }

    @Given("^at least two accounts have already been provisioned with £(.+) balance$")
    public void at_least_two_accounts_have_already_been_provisioned_with_£_balance(double amount) throws Throwable {
        BigDecimal balance = BigDecimal.valueOf(amount);
        accountDao.save(TestDataHelper.getAccount(null, FROM_ACCOUNT_NUMBER, FROM_SORT_CODE, balance));
        accountDao.save(TestDataHelper.getAccount(null, TO_ACCOUNT_NUMBER, TO_SORT_CODE, balance));
    }

    @When("^users/clients make a request to transfer £(.+) between from/to accounts$")
    public void users_clients_make_a_request_to_transfer_£_between_from_to_accounts(double amount) throws IOException {

        makeRequest(amount, anAccountDto().withNumber(FROM_ACCOUNT_NUMBER).withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build());
    }

    private void makeRequest(double amount, AccountDto to, AccountDto from) throws IOException {
        TransferRequest transferRequest = TransferRequest.TransferRequestBuilder.aTransferRequest()
                .withTo(to)
                .withFrom(from)
                .withNote("Automated transfer")
                .withAmount(BigDecimal.valueOf(amount))
                .build();

        String request = gson.toJson(transferRequest);

        HttpUriRequest httpUriRequest = RequestBuilder.post(TRANSFER_SVC_URL)
                .setEntity(new StringEntity(request))
                .addHeader("Accept", APPLICATION_JSON)
                .addHeader("Content-type", APPLICATION_JSON)
                .build();

        CloseableHttpClient client = HttpClients.createDefault();

        response = client.execute(httpUriRequest);
    }

    @Then("^from account's balance should have been £(.+)$")
    public void from_account_s_balance_should_have_been_£(double balance) throws Throwable {
        BigDecimal actualBalance = accountDao.findAccountByNumberAndSortCode(TO_ACCOUNT_NUMBER, TO_SORT_CODE)
                .map(Account::getBalance)
                .get();
        assertThat(actualBalance).isEqualTo(BigDecimal.valueOf(balance));
    }

    @Then("^to account's balance should have been £(.+)$")
    public void to_account_s_balance_should_have_been_£(double balance) throws Throwable {
        BigDecimal actualBalance = accountDao.findAccountByNumberAndSortCode(FROM_ACCOUNT_NUMBER, FROM_SORT_CODE)
                .map(Account::getBalance)
                .get();
        assertThat(actualBalance).isEqualTo(BigDecimal.valueOf(balance));
    }

    @Then("^the response should have had transfer id with a status message\\.$")
    public void the_response_should_have_had_transfer_id_with_a_status_message() throws Throwable {

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(response.getFirstHeader(CONTENT_TYPE).getValue()).isEqualTo(APPLICATION_JSON);

        String responseBody = EntityUtils.toString(response.getEntity());
        TransferResponse transferResponse = gson.fromJson(responseBody, TransferResponse.class);
        assertThat(transferResponse).isNotNull();
        assertThat(transferResponse.getTransferId()).isEqualTo(1);
        assertThat(transferResponse.getMessage()).isEqualTo("Transfer has been processed successfully");
    }

    @Then("^the response should have had insufficient fund message\\.$")
    public void the_response_should_have_had_insufficient_fund_message() throws Throwable {

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
        assertThat(response.getFirstHeader(CONTENT_TYPE).getValue()).isEqualTo(APPLICATION_JSON);

        String responseBody = EntityUtils.toString(response.getEntity());
        TransferResponse transferResponse = gson.fromJson(responseBody, TransferResponse.class);
        assertThat(transferResponse).isNotNull();
        assertThat(transferResponse.getTransferId()).isNull();
        assertThat(transferResponse.getMessage()).isEqualTo("From account has insufficient fund");

    }

    @When("^users/clients make a request that has invalid account details$")
    public void users_clients_make_a_request_that_has_invalid_account_details() throws Throwable {
        makeRequest(12.99, anAccountDto().withNumber("00813711").withSortCode(FROM_SORT_CODE).build(),
                anAccountDto().withNumber(TO_ACCOUNT_NUMBER).withSortCode(TO_SORT_CODE).build());
    }

    @Then("^the response should have had account not exist message\\.$")
    public void the_response_should_have_had_account_not_exist_message() throws Throwable {

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
        assertThat(response.getFirstHeader(CONTENT_TYPE).getValue()).isEqualTo(APPLICATION_JSON);

        String responseBody = EntityUtils.toString(response.getEntity());
        TransferResponse transferResponse = gson.fromJson(responseBody, TransferResponse.class);
        assertThat(transferResponse).isNotNull();
        assertThat(transferResponse.getTransferId()).isNull();
        assertThat(transferResponse.getMessage()).isEqualTo("Account not found, provided accountNumber: 00813711, sortCode: 800551");
    }

    @When("^users/clients make a request to retrieve the transfer details by its id, (\\d+)$")
    public void users_clients_make_a_request_to_retrieve_the_transfer_details_by_its_id(int id) throws IOException {

        HttpUriRequest httpUriRequest = RequestBuilder.get(TRANSFER_SVC_URL + "/" + id)
                .addHeader("Accept", APPLICATION_JSON)
                .addHeader("Content-type", APPLICATION_JSON)
                .build();

        CloseableHttpClient client = HttpClients.createDefault();

        response = client.execute(httpUriRequest);
    }

    @Then("^the response should have had transfer details$")
    public void the_response_should_have_had_transfer_details() throws IOException {

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(response.getFirstHeader(CONTENT_TYPE).getValue()).isEqualTo(APPLICATION_JSON);

        String responseBody = EntityUtils.toString(response.getEntity());
        TransferDto transferDto = gson.fromJson(responseBody, TransferDto.class);

        assertThat(transferDto).isNotNull();
        assertThat(transferDto).isEqualTo(TestDataHelper.aTransferDto());
    }

    @Then("^the response should have had transfer not exist message$")
    public void the_response_should_have_had_transfer_not_exist_message() throws Throwable {

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
        assertThat(response.getFirstHeader(CONTENT_TYPE).getValue()).isEqualTo(APPLICATION_JSON);

        String responseBody = EntityUtils.toString(response.getEntity());
        TransferResponse transferResponse = gson.fromJson(responseBody, TransferResponse.class);
        assertThat(transferResponse).isNotNull();
        assertThat(transferResponse.getTransferId()).isNull();
        assertThat(transferResponse.getMessage()).isEqualTo("Transfer not found for given id 999");
    }

}
