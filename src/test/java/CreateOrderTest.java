import client.OrderClient;
import client.UserClient;
import ordermodel.Order;
import usermodel.User;
import usermodel.UserCredentials;
import usermodel.UserGenerator;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private String userToken;

    @Before
    @Description("Инициализация данных")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        order = new Order();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    @Description("Очистка данных")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Заказ авторизованным пользователем с валидными ингредиентами")
    @Description("Проверяем, что авторизованный пользователь может создать заказ с валидными ингредиентами")
    public void orderCreateWithLoginAndIngredients() {

        order = Order.makeIngredients();
        userToken = userClient.makeUser(user).extract().path("accessToken");
        userClient.loginUser(UserCredentials.from(user));

        orderClient.makeOrderByAuthorizedUser(order, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("order._id", notNullValue());
    }

    @Test
    @DisplayName("Заказ авторизованным пользователем без ингредиентов")
    @Description("Проверяем, что авторизованный пользователь не может создать заказ без ингредиентов")
    public void orderCreateWithLoginAndWithoutIngredients() {
        userToken = userClient.makeUser(user).extract().path("accessToken");
        userClient.loginUser(UserCredentials.from(user));

        orderClient.makeOrderByAuthorizedUser(order, userToken)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Заказ неавторизованным пользователем c ингредиентами")
    @Description("Проверяем возможность создания заказа с ингредиентами неавторизованным пользователем")
    public void orderCreateWithoutLoginAndWithIngredients() {
        order = Order.makeIngredients();
        orderClient.makeOrderByUnloggedUser(order)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Заказ неавторизованным пользователем без ингредиентов")
    @Description("Проверяем, что неавторизованный пользователь не может создать заказ без ингредиентов")
    public void orderCreateWithoutLoginAndWithoutIngredients() {

        orderClient.makeOrderByUnloggedUser(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Заказ с невалидным хешем ингредиентов")
    @Description("Проверяем, что нельзя создать заказ с невалидными ингредиентами")
    public void orderCreateWithInvalidHashIngredients() {
        order = Order.makeIngredientsWithIncorrectHash();
        orderClient.makeOrderByUnloggedUser(order)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
