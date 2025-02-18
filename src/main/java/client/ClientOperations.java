package client;//

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import constant.Constants;
import static io.restassured.RestAssured.given;


public class ClientOperations {

    @Step("Создать пользователя")
    public static Response createUser(Client client) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(client)
                .when()
                .post(Constants.REGISTER_PATH);
        return response;
    }

    @Step("Войти в систему как пользователь")
    public static Response logInUser(Client client) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(client)
                .when()
                .post(Constants.LOGIN_PATH);
        return response;
    }
    //
    @Step("Удалить пользователя")
    public static void deleteUser(String accessToken) {
        if (accessToken != null)
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(Constants.USER_PATH);
    }

    @Step("Редактировать авторизованного пользователя")
    public static Response editAuthorizedUser(String accessToken, ClientEditedData clientEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(clientEditedData)
                .when()
                .patch(Constants.USER_PATH);
        return response;
    }

    @Step("Редактировать неавторизованного пользователя")
    public static Response editUnauthorizedUser(String accessToken, ClientEditedData clientEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(clientEditedData)
                .when()
                .patch(Constants.USER_PATH);
        return response;
    }

    public static Response getUser(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken) // Передаем токен авторизации
                .when()
                .get("/user"); // Укажите правильный эндпоинт для получения данных пользователя
    }
}





