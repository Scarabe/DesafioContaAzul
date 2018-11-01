package api.desafio.contaazul.integration;

import static api.desafio.contaazul.enums.BankSlipStatusEnum.PENDING;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import api.desafio.contaazul.dto.BankSlipFullDetailsDTO;
import api.desafio.contaazul.dto.InsertedBankSlipDTO;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author gscarabelo
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 10/31/18 3:02 PM
 */
public class BankSlipControllerTest {

    private Map<String, Object> insertData = new HashMap<>();

    private String BASE_URL = "http://localhost:8080/rest";

    private String CONTENT_TYPE = "application/json";

    @Before
    public void setUp() throws Exception {
        insertData.put("due_date", "2018-01-01");
        insertData.put("total_in_cents", "100000");
        insertData.put("customer", "Trillian Company");
    }

    @Test
    public void whenGiveRightData() throws ParseException {
        final InsertedBankSlipDTO insertedBankSlipDTO = new InsertedBankSlipDTO();
        insertedBankSlipDTO.setDue_date(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
        insertedBankSlipDTO.setTotal_in_cents(new BigDecimal(100000));
        insertedBankSlipDTO.setCustomer("Trillian Company");
        insertedBankSlipDTO.setStatus(PENDING);

        final Response response = RestAssured.given()
                .body(insertData)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips");
        final InsertedBankSlipDTO retDto = new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class);
        insertedBankSlipDTO.setId(retDto.getId());

        Assert.assertEquals(response.statusCode(), CREATED.value());
        Assert.assertEquals(insertedBankSlipDTO, retDto);
    }

    @Test
    public void whenInsertIsNull() {
        RestAssured.given()
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void wheInsertHasCustomerHasNullData() {
        insertData.put("customer", null);
        RestAssured.given()
                .body(insertData)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void wheInsertHasCentsHasNullData() {
        insertData.put("total_in_cents", null);
        RestAssured.given()
                .body(insertData)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void whenInsertHasDateHasNullData() {
        insertData.put("due_date", null);
        RestAssured.given()
                .body(insertData)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void bankSlipList() {
        populate();
        final Response response = RestAssured.given()
                .contentType(CONTENT_TYPE)
                .when().get(BASE_URL + "/bankslips/");
        final Type listType = new TypeToken<ArrayList<InsertedBankSlipDTO>>() {
        }.getType();
        final List<InsertedBankSlipDTO> retDto = new Gson().fromJson(response.asString(), listType);
        Assert.assertEquals(response.statusCode(), OK.value());
        Assert.assertTrue(retDto.size() >= 2);
    }

    @Test
    public void whenRequestDetailedBankSlip() {
        final InsertedBankSlipDTO insertedBankSlipDTO = populate();
        final Response response = getBankSlipDetail(insertedBankSlipDTO.getId());
        Assert.assertEquals(response.statusCode(), OK.value());
        Assert.assertEquals(new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class), insertedBankSlipDTO);
    }

    @Test
    public void whenequestDetailedBankSlipWithRandomId() {
        RestAssured.given()
                .pathParam("id", UUID.randomUUID())
                .contentType(CONTENT_TYPE)
                .when().get(BASE_URL + "/bankslips/{id}").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void whenMakeAPayment() {
        final InsertedBankSlipDTO insertedBankSlipDTO = populate();
        Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-06-30");
        final Response paymentResponse = makePayment(insertedBankSlipDTO.getId(), payment);
        Assert.assertEquals(paymentResponse.statusCode(), NO_CONTENT.value());
    }

    @Test
    public void whenTryToMakeAPaymentWithRandomId() {
        final Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-06-30");
        final Response paymentResponse = makePayment(UUID.randomUUID(), payment);
        Assert.assertEquals(paymentResponse.statusCode(), NOT_FOUND.value());
    }

    @Test
    public void whenCancel() {
        final InsertedBankSlipDTO insertedBankSlipDTO = populate();
        RestAssured.given()
                .pathParam("id", insertedBankSlipDTO.getId())
                .contentType(CONTENT_TYPE)
                .when().delete(BASE_URL + "/bankslips/{id}").then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    public void whenTryToCancelWithRandomId() {
        RestAssured.given()
                .pathParam("id", UUID.randomUUID())
                .contentType(CONTENT_TYPE)
                .when().delete(BASE_URL + "/bankslips/{id}").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void whenHasPaymentAndFineBeforeTenDays() {
        final Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-01-05");
        final InsertedBankSlipDTO insertedBankSlipDTO = populate();
        final UUID uuid = insertedBankSlipDTO.getId();
        makePayment(uuid, payment);
        final Response response = getBankSlipDetail(uuid);
        Assert.assertEquals(response.statusCode(), OK.value());
        BankSlipFullDetailsDTO bankSlipFullDetailsDTO = new Gson().fromJson(response.asString(),
                BankSlipFullDetailsDTO.class);
        Assert.assertEquals(bankSlipFullDetailsDTO.getFine(),
                insertedBankSlipDTO.getTotal_in_cents().multiply(new BigDecimal("0.005")).setScale(0, RoundingMode.UP));
    }

    @Test
    public void whenHasPaymentAndFineAfterDays() {
        final Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-01-20");
        final InsertedBankSlipDTO insertedBankSlipDTO = populate();
        final UUID uuid = insertedBankSlipDTO.getId();
        makePayment(uuid, payment);
        final Response response = getBankSlipDetail(uuid);
        Assert.assertEquals(response.statusCode(), OK.value());
        BankSlipFullDetailsDTO bankSlipFullDetailsDTO = new Gson().fromJson(response.asString(),
                BankSlipFullDetailsDTO.class);
        Assert.assertEquals(bankSlipFullDetailsDTO.getFine(),
                insertedBankSlipDTO.getTotal_in_cents().multiply(new BigDecimal("0.01")).setScale(0, RoundingMode.UP));
    }

    private Response getBankSlipDetail(final UUID uuid) {
        return RestAssured.given()
                .pathParam("id", uuid)
                .contentType(CONTENT_TYPE)
                .when().get(BASE_URL + "/bankslips/{id}");
    }

    private Response makePayment(final UUID uuid, final Map<String, Object> payment) {
        return RestAssured.given()
                .pathParam("id", uuid)
                .body(payment)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips/{id}/payments");

    }

    private InsertedBankSlipDTO populate() {
        final Map<String, Object> dataToInsert = new HashMap<>();
        dataToInsert.put("due_date", "2018-01-01");
        dataToInsert.put("total_in_cents", "100000");
        dataToInsert.put("customer", "Ford Prefect Company");
        return callInsert(dataToInsert);
    }

    private InsertedBankSlipDTO callInsert(Map<String, Object> dataToInsert) {
        final Response response = RestAssured.given()
                .body(dataToInsert)
                .contentType(CONTENT_TYPE)
                .when().post(BASE_URL + "/bankslips");
        return new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class);
    }
}
