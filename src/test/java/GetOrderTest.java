import client.OrderClient;
import client.UserClient;
import ordermodel.Order;
import usermodel.User;
import usermodel.UserCredentials;
import usermodel.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderTest {
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
        order = Order.makeIngredients();
    }

    @After
    @Description("Очистка данных")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Успешное получение заказа авторизованного пользователя")
    @Description("Проверяем возможность получения информации о заказе авторизованным пользователем")
    public void getOrderByLoginUser() {
        userToken = userClient.makeUser(user).extract().path("accessToken");
        userClient.loginUser(UserCredentials.from(user));
        ValidatableResponse responseOrder = orderClient.makeOrderByAuthorizedUser(order, userToken);
        String nameBurger = responseOrder.extract().path("name");

        orderClient.getOrderByAuthorizedUser(userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("orders[0].name", equalTo(nameBurger));
    }

    @Test
    @DisplayName("Получение заказа пользователя без авторизации")
    @Description("Проверяем возможность получения заказа неавторизованным пользователем")
    public void getOrderByWithoutLogin() {
        orderClient.makeOrderByUnloggedUser(order);
        orderClient.getOrderByUnloggedUser()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }


}
