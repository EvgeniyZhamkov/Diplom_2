package orders;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import constant.Constants;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrdersUsers {
    @Step
    public static Response getOrdersAuthorizedUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .when()
                .get(Constants.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response getOrdersUnauthorizedUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(Constants.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response createOrder(String accessToken, Orders orders) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(orders)
                .when()
                .post(Constants.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response createOrderWithoutAuth(Orders orders) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(orders)
                .when()
                .post(Constants.ORDERS_PATH);
        return response;
    }

    @Step
    public static List<String> getAllIngredients() {
        List<String> allIngredients = given()
                .header("Content-type", "application/json")
                .when()
                .get(Constants.INGREDIENTS_PATH)
                .then()
                .extract()
                .path("data._id");
        return allIngredients;
    }
}

