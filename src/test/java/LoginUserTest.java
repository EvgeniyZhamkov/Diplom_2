import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.Client;
import constant.Random;
import client.ClientOperations;
import constant.Constants;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {

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
    @DisplayName("Войдите в систему, используя правильные данные")
    @Description("Войдите в систему, используя правильные данные")
    public void logInGetSuccess() {
        Response responseCreating = ClientOperations.createUser(client);
        //accessToken нужен для последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        Response responseLogin = ClientOperations.logInUser(client);
        responseLogin.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Войдите в систему с неверным паролем")
    @Description("Войдите в систему с неверным паролем")
    public void logInWithIncorrectPasswordGetError() {
        ClientOperations.createUser(client);
        Client incorrectClient = new Client(client.getEmail(), RandomStringUtils.randomAlphabetic(10), client.getName());
        ClientOperations.logInUser(incorrectClient)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Войдите в систему с неверным адресом электронной почты")
    @Description("Войдите в систему с неверным адресом электронной почты")
    public void logInWithIncorrectEmailGetError() {
        ClientOperations.createUser(client);
        Client incorrectClient = new Client(RandomStringUtils.randomAlphabetic(10) + "@gmail.com", client.getPassword(), client.getName());
        ClientOperations.logInUser(incorrectClient)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}

