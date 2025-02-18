import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrdersUsers;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.Client;
import client.ClientOperations;
import constant.Constants;
import constant.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetUserOrdersTest {

    Client client;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        client = Random.generateUser();
    }

    @After
    public void tearDown() {
        ClientOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Получать заказы от авторизованного пользователя")
    @Description("Получать заказы от авторизованного пользователя")
    public void getOrdersAuthorizedUserGetSuccess() {
        Response response = ClientOperations.createUser(client);
        //accessToken нужен для создания заказа и последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrdersUsers.getOrdersAuthorizedUser(accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получать заказы от неавторизованного пользователя")
    @Description("Получать заказы от неавторизованного пользователя")
    public void getOrdersUnauthorizedUserGetError() {
        Response response = ClientOperations.createUser(client);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrdersUsers.getOrdersUnauthorizedUser()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
