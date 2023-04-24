package orderTests;

import base.Base;
import user.*;
import ingredient.*;
import order.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateOrderTests extends Base {
    private final UserGenerator generator = new UserGenerator();
    private final OrderStatuses status = new OrderStatuses();
    private final IngredientStatuses ingredientStatus = new IngredientStatuses();

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Test
    @DisplayName("Создание нового заказа без ингредиентов после авторизации")
    @Description("В данном сценарии проверяется, что создание заказа авторизованным пользователем без ингредиентов невозможно")
    public void createNewOrderAfterAuthorizationButWithoutIngredients() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        OrderClient.createOrderWithAuthorization(userResponse, null)
                .then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание нового заказа с ингредиентами после авторизации")
    @Description("В данном сценарии проверяется, что работает создание заказа с ингредиентами авторизованным пользователем")
    public void createNewOrderWithIngredientsAfterAuthorization() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        List<String> ingredients = new ArrayList<>();
        response = IngredientClient.getIngredientsList();
        ingredientStatus.ingredientListGotSuccessfully(response);

        Ingredients ingredientsForAdd = response.as(Ingredients.class);
        Ingredient ingredient = ingredientsForAdd.getIngredientWithName("Флюоресцентная булка R2-D3");
        ingredients.add(ingredient.getId());


        Order order = new Order();
        order.setIngredients(ingredients);
        OrderClient.createOrderWithAuthorization(userResponse, order);
        status.createdSuccessfully(response);
    }

    @Test
    @DisplayName("Создание нового заказа без ингредиентов без авторизации")
    @Description("В данном сценарии проверяется, что создание заказа неавторизованным пользователем без ингредиентов невозможно")
    public void createNewOrderWithoutIngredientsWithoutAuthorization() {
        OrderClient.createOrderWithoutAuthorization(null)
                .then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание нового заказа с ингредиентами без авторизации")
    @Description("В данном сценарии проверяется, что работает создание заказа с ингредиентами неавторизованным пользователем")
    public void createNewOrderWithoutAuthorizationButWithIngredients() {
        List<String> ingredients = new ArrayList<>();
        Response response = IngredientClient.getIngredientsList();
        ingredientStatus.ingredientListGotSuccessfully(response);

        Ingredients ingredientsForAdd = response.as(Ingredients.class);
        Ingredient ingredient = ingredientsForAdd.getIngredientWithName("Кристаллы марсианских альфа-сахаридов");
        ingredients.add(ingredient.getId());


        Order order = new Order();
        order.setIngredients(ingredients);
        OrderClient.createOrderWithoutAuthorization(order);
        status.createdSuccessfully(response);
    }

    @Test
    @DisplayName("Создание нового заказа с неущестующими ингредиентами после авторизации")
    @Description("В данном сценарии проверяется, что создание заказа авторизованным пользователем с неверными ингредиентами невозможно")
    public void createNewOrderWithAuthorizationButWithWrongIngredients() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;
        List<String> ingredients = new ArrayList<>();
        ingredients.add("1-й ингредиент");
        ingredients.add("2-й ингредиент");
        ingredients.add("3-й ингредиент");

        Order order = new Order();
        order.setIngredients(ingredients);

        OrderClient.createOrderWithAuthorization(userResponse, order)
                .then().assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
