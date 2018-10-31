package api.desafio.contaazul.integration;

import static api.desafio.contaazul.enums.BankSlipStatusEnum.PENDING;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    private InsertedBankSlipDTO iBSDTO = new InsertedBankSlipDTO();

    private Map<String, Object> insertData = new HashMap<>();

    @Before
    public void setup() throws Exception {
        iBSDTO.setDue_date(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
        iBSDTO.setTotal_in_cents(new BigDecimal(100000));
        iBSDTO.setCustomer("Trillian Company");
        iBSDTO.setStatus(PENDING);

        insertData.put("due_date", "2018-01-01");
        insertData.put("total_in_cents", "100000");
        insertData.put("customer", "Trillian Company");
    }

    @Test
    public void whenGiveRightData() {
        Response response = RestAssured.given()
                .body(insertData)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips");
        InsertedBankSlipDTO retDto = new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class);
        iBSDTO.setId(retDto.getId());

        Assert.assertEquals(response.statusCode(), CREATED.value());
        Assert.assertEquals(iBSDTO, retDto);
    }

    @Test
    public void whenInsertIsNull() {
        RestAssured.given()
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void wheInsertHasCustomerHasNullData() {
        insertData.put("customer", null);
        RestAssured.given()
                .body(insertData)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void wheInsertHasCentsHasNullData() {
        insertData.put("total_in_cents", null);
        RestAssured.given()
                .body(insertData)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void whenInsertHasDateHasNullData() {
        insertData.put("due_date", null);
        RestAssured.given()
                .body(insertData)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips").then()
                .statusCode(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void bankSlipList() {
        populate();
        Response response = RestAssured.given()
                .contentType("application/json")
                .when().get("http://localhost:8080/rest/bankslips/");
        Type listType = new TypeToken<ArrayList<InsertedBankSlipDTO>>() {
        }.getType();
        List<InsertedBankSlipDTO> retDto = new Gson().fromJson(response.asString(), listType);
        Assert.assertEquals(response.statusCode(), OK.value());
        Assert.assertTrue(retDto.size() >= 2);
    }

    @Test
    public void whenRequestDetailedBankSlip() {
        InsertedBankSlipDTO insertedBankSlipDTO = populate().get(0);
        Response response = RestAssured.given()
                .pathParam("id", insertedBankSlipDTO.getId())
                .contentType("application/json")
                .when().get("http://localhost:8080/rest/bankslips/{id}");
        Assert.assertEquals(response.statusCode(), OK.value());
        Assert.assertEquals(new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class),
                insertedBankSlipDTO);
    }

    @Test
    public void whenequestDetailedBankSlipWithRandomId() {
        RestAssured.given()
                .pathParam("id", UUID.randomUUID())
                .contentType("application/json")
                .when().get("http://localhost:8080/rest/bankslips/{id}").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void whenMakeAPayment() {
        InsertedBankSlipDTO insertedBankSlipDTO = populate().get(0);

        Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-06-30");
        RestAssured.given()
                .pathParam("id", insertedBankSlipDTO.getId())
                .body(payment)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips/{id}/payments").then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    public void whenTryToMakeAPaymentWithRandomId() {
        Map<String, Object> payment = new HashMap<>();
        payment.put("payment_date", "2018-06-30");
        RestAssured.given()
                .pathParam("id", UUID.randomUUID())
                .body(payment)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips/{id}/payments").then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void whenCancel() {
        InsertedBankSlipDTO insertedBankSlipDTO = populate().get(0);
        Map<String, Object> payment = new HashMap<>();
        RestAssured.given()
                .pathParam("id", insertedBankSlipDTO.getId())
                .contentType("application/json")
                .when().delete("http://localhost:8080/rest/bankslips/{id}").then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    public void whenTryToCancelWithRandomId() {
        RestAssured.given()
                .pathParam("id", UUID.randomUUID())
                .contentType("application/json")
                .when().delete("http://localhost:8080/rest/bankslips/{id}").then()
                .statusCode(NOT_FOUND.value());
    }

    private List<InsertedBankSlipDTO> populate() {
        List<InsertedBankSlipDTO> insertedBankSlipDTO = new ArrayList<>();
        Map<String, Object> dataToInsert = new HashMap<>();
        dataToInsert.put("due_date", "2018-01-01");
        dataToInsert.put("total_in_cents", "100000");
        dataToInsert.put("customer", "Ford Prefect Company");
        insertedBankSlipDTO.add(callInsert(dataToInsert));
        return insertedBankSlipDTO;
    }

    private InsertedBankSlipDTO callInsert(Map<String, Object> dataToInsert) {
        Response response = RestAssured.given()
                .body(dataToInsert)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips");
        return new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class);
    }
}
