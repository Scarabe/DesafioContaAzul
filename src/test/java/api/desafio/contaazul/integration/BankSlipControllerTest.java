package api.desafio.contaazul.integration;

import static api.desafio.contaazul.enums.BankSlipStatusEnum.PENDING;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import api.desafio.contaazul.dto.InsertedBankSlipDTO;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * @author gscarabelo
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 10/31/18 3:02 PM
 */
public class BankSlipControllerTest {

    private InsertedBankSlipDTO insertedBankSlipDTO = new InsertedBankSlipDTO();

    @Before
    public void setup() throws Exception {
        insertedBankSlipDTO.setDue_date(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
        insertedBankSlipDTO.setTotal_in_cents(new BigDecimal(100000));
        insertedBankSlipDTO.setCustomer("Trillian Company");
        insertedBankSlipDTO.setStatus(PENDING);
    }

    @Test
    public void whenGiveRightData() {
        Map<String, Object> map = new HashMap<>();
        map.put("due_date", "2018-01-01");
        map.put("total_in_cents", "100000");
        map.put("customer", "Trillian Company");
        Response response = RestAssured.given()
                .body(map)
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips");
        InsertedBankSlipDTO retDto = new Gson().fromJson(response.asString(), InsertedBankSlipDTO.class);
        insertedBankSlipDTO.setId(retDto.getId());
        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(insertedBankSlipDTO, retDto);
    }

    @Test
    public void whenInsertIsNull() {

        RestAssured.given()
                .contentType("application/json")
                .when().post("http://localhost:8080/rest/bankslips").then()
                .statusCode(404);
    }

}
