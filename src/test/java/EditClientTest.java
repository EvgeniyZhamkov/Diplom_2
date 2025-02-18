import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.Client;
import client.ClientEditedData;
import constant.Random;
import client.ClientOperations;
import constant.Constants;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class EditClientTest {

    Client client;
    ClientEditedData clientEditedData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        client = Random.generateUser();
        clientEditedData = Random.generateUserEditedData();
    }

    @After
    public void tearDown() {
        ClientOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Редактировать электронную почту авторизованного пользователя")
    @Description("Редактировать электронную почту авторизованного пользователя")
    public void editEmailAuthorizedUserGetSuccess() {

        Response responseCreating = ClientOperations.createUser(client);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        ClientOperations.editAuthorizedUser(accessToken, clientEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Редактировать электронную почту неавторизованного пользователя")
    @Description("Редактировать электронную почту неавторизованного пользователя")
    public void editEmailUnauthorizedUserGetError() {

        Response responseCreating = ClientOperations.createUser(client);
        //accessToken нужен для редактирования и последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        ClientOperations.editUnauthorizedUser(accessToken, clientEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}


