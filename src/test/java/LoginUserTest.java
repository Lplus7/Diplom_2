import client.UserClient;
import usermodel.User;
import usermodel.UserCredentials;
import usermodel.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    private UserCredentials userCredentials;
    private String userToken;


    @Before
    @Description("Инициализация данных")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userToken = userClient.makeUser(user).extract().path("accessToken");
    }

    @After
    @Description("Очистка данных")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверка логина пользователя с валидными данными")
    public void loginUserTest() {
        userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация с невалидной почтой")
    @Description("Проверка авторизации пользователя с некорректной почтой")
    public void loginUserWithIncorrectEmailTest() {
        userCredentials = UserCredentials.withIncorrectEmail(user);
        userClient.loginUser(userCredentials)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с невалидным паролем")
    @Description("Проверка авторизации пользователя с некорректным паролем")
    public void loginUserWithIncorrectPasswordTest() {
        userCredentials = UserCredentials.withIncorrectPassword(user);
        userClient.loginUser(userCredentials)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
