import client.UserClient;
import usermodel.User;
import usermodel.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest {
    private User user;
    private UserClient userClient;
    private String userToken;

    @Before
    @Description("Инициализация данных")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @After
    @Description("Очистка данных")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя с корректными данными")
    @Description("Проверка, что пользователь успешно создается")
    public void createNewValidUserTest() {
        userToken = userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверка невозможности создания пользователей с одинаковыми логинами")
    public void makeDuplicateUserTest() {

        userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));

        userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполненного поля почта")
    @Description("Проверка невозможности создания пользователя без заполненного поля с почтой")
    public void makeUserWithoutEmailTest() {
        user = UserGenerator.getUserWithoutEmail();

        userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполненного поля пароль")
    @Description("Проверка невозможности создания пользователя без пароля")
    public void makeUserWithoutPasswordTest() {
        user = UserGenerator.getUserWithoutPassword();

        userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполненного поля имя")
    @Description("Проверка невозможности создания пользователя без заполненного поля с именем")
    public void makeUserWithoutNameTest() {
        user = UserGenerator.getUserWithoutName();

        userClient.makeUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}