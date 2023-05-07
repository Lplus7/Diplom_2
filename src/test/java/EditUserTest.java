import client.UserClient;
import usermodel.User;
import usermodel.UserCredentials;
import usermodel.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class EditUserTest {
    private User user;
    private UserClient userClient;
    private String userToken;
    private User newUser;

    @Before
    @Description("Инициализация данных")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userToken = userClient.makeUser(user).extract().path("accessToken");
        newUser = UserGenerator.getRandomUser();
    }

    @After
    @Description("Очистка данных")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Редактирование данных пользователя после авторизации")
    @Description("Проверка возможности внесения изменений в данные авторизованного пользователя")
    public void editUserWithLoginTest() {
        userClient.loginUser(UserCredentials.from(user));
        userClient.editUserWithLogin(newUser, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.email", equalTo(newUser.getEmail().toLowerCase(Locale.ROOT)))
                .and()
                .body("user.name", equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Редактирование данных неавторизованного пользователя")
    @Description("Проверка невозможности внесения изменений в данные неавторизованным пользователем")
    public void editUserWithoutLoginTest() {
        userClient.editUserWithoutLogin(newUser)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", is(false));
    }
}



