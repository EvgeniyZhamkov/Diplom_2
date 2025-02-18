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
        // Создаем нового пользователя
        Response responseCreating = ClientOperations.createUser(client);

        // Извлекаем accessToken для последующего использования
        String accessToken = responseCreating.then().extract().path("accessToken").toString();

        // Редактируем данные пользователя
        Response responseEdit = ClientOperations.editAuthorizedUser(accessToken, clientEditedData);

        // Проверяем статус-код и основные поля в ответе
        responseEdit.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK) // Проверяем, что код ответа равен 200
                .and()
                .body("success", equalTo(true)) // Проверяем, что success = true
                .and()
                .body("user", notNullValue()) // Проверяем, что поле user не null
                .and()
                .body("user.email", equalTo(client.getEmail())) // Проверяем, что email обновился корректно
                .and()
                .body("user.name", equalTo(client.getName())); // Проверяем, что name обновился корректно
    }

    @Test
    @DisplayName("Редактировать имя авторизованного пользователя")
    @Description("Редактировать имя авторизованного пользователя")
    public void editNameAuthorizedUserGetSuccess() {
        // Создаем нового пользователя
        Response responseCreating = ClientOperations.createUser(client);

        // Извлекаем accessToken для последующего использования
        String accessToken = responseCreating.then().extract().path("accessToken").toString();

        // Редактируем данные пользователя (в данном случае, имя)
        Response responseEdit = ClientOperations.editAuthorizedUser(accessToken, clientEditedData);

        // Проверяем статус-код и основные поля в ответе
        responseEdit.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK) // Проверяем, что код ответа равен 200
                .and()
                .body("success", equalTo(true)) // Проверяем, что success = true
                .and()
                .body("user", notNullValue()) // Проверяем, что поле user не null
                .and()
                .body("user.name", equalTo(client.getName())) // Проверяем, что имя обновилось корректно
                .and()
                .body("user.email", equalTo(client.getEmail())); // Проверяем, что email остался без изменений
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


