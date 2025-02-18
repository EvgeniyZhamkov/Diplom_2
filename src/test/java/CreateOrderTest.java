import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.Orders;
import orders.OrdersUsers;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.Client;
import client.ClientOperations;
import constant.Constants;
import constant.Random;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    Client client;
    Orders orders;
    String accessToken;
    int ingredientSublistSize;
    List<String> ingredients;
    List<String> allIngredients;


    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        allIngredients = OrdersUsers.getAllIngredients();

        client = Random.generateUser();
        Response response = ClientOperations.createUser(client);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
    }

    @After
    public void tearDown() {
        ClientOperations.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Авторизованный пользователь создает правильный заказ с использованием ингредиентов")
    @Description("Авторизованный пользователь создает правильный заказ с использованием ингредиентов")
    public void createCorrectOrderWithIngredientsAuthorizedUserGetSuccess() {
        // Генерация подсписка ингредиентов
        ingredientSublistSize = Random.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);

        // Создание заказа
        orders = new Orders(ingredients);

        // Отправка запроса и проверка ответа
        OrdersUsers.createOrder(accessToken, orders)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }


    @Test
    @DisplayName("Авторизованный пользователь может создать заказ без ингредиентов")
    @Description("Авторизованный пользователь может создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsAuthorizedUserGetError() {
        orders = new Orders(ingredients);
        OrdersUsers.createOrder(accessToken, orders)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Авторизованный пользователь создает заказ с недопустимым ингредиентом")
    @Description("Авторизованный пользователь создает заказ с недопустимым ингредиентом")
    public void createOrderInvalidIngredientAuthorizedUserGetSuccess() {
        ingredients = new ArrayList<>();
        ingredients.add(RandomStringUtils.randomAlphabetic(24));
        orders = new Orders(ingredients);
        OrdersUsers.createOrder(accessToken, orders)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизованным пользователем")
    @Description("Создание заказа с ингредиентами неавторизованным пользователем")
    public void createOrderWithIngredientsUnauthorizedUserGetError() {
        ingredientSublistSize = Random.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        orders = new Orders(ingredients);
        OrdersUsers.createOrderWithoutAuth(orders)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}

