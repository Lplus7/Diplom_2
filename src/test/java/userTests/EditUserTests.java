package userTests;

import base.Base;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.*;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import user.*;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class EditUserTests extends Base{
    private final UserGenerator generator = new UserGenerator();
    private final UserStatuses status = new UserStatuses();

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Test
    @DisplayName("Редактирование имени пользователя после авторизации")
    @Description("В данном сценарии проверяется, что авторизованный пользователь может сменить своё имя")
    public void userNameCanBeEditedAfterAuthorization() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        user.setName(UserGenerator.generateName());
        UserClient.editUserWithToken(userResponse, user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Редактирование почты пользователя после авторизации")
    @Description("В данном сценарии проверяется, что авторизованный пользователь может сменить свою почту")
    public void userEmailCanBeEditedAfterAuthorization() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        user.setEmail(UserGenerator.generateEmail());
        UserClient.editUserWithToken(userResponse, user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Редактирование пароля пользователя после авторизации")
    @Description("В данном сценарии проверяется, что авторизованный пользователь может сменить свой пароль")
    public void userPasswordCanBeEditedAfterAuthorization() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        user.setPassword(UserGenerator.generatePassword());
        UserClient.editUserWithToken(userResponse, user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Редактирование данных пользователя без авторизации")
    @Description("В данном сценарии проверяется, что неавторизованный пользователь не может сменить свои данные")
    public void userDataCantBeEditedWithoutAuthorization() {
        User user = generator.randomUser();
        userResponse = UserClient.createUser(user).as(UserResponse.class);

        Response response = UserClient.editUserWithoutToken(user);
        status.youShouldBeAuthorised(response);
    }

    @Test
    @DisplayName("Редактирование почты пользователя после авторизации на уже принадлежащую другому")
    @Description("В данном сценарии проверяется, что авторизованный пользователь не может сменить почту на ту, " +
            "которая уже используется другим пользователем")
    public void userEmailCantBeEditedToDuplicate() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;

        user.setEmail("test-data@yandex.ru"); //почта из примера в документации к API
        UserClient.editUserWithToken(userResponse, user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }
}
