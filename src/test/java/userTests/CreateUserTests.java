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


public class CreateUserTests extends Base {
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
    @DisplayName("Создание нового пользователя")
    @Description("В данном сценарии проверяется, что новый пользователь может быть создан")
    public void userCanBeCreated() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("В данном сценарии проверяется, что пользователь не может быть создан с такими же данными")
    public void duplicateUserCantBeCreated() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        response = UserClient.createUser(user);
        status.userAlreadyExists(response);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("В данном сценарии проверяется, что создание пользователя без имени невозможно")
    public void userWithoutNameCantBeCreated() {
        User user = generator.randomUser();
        user.setName(null);
        Response response = UserClient.createUser(user);
        status.emailPasswordAndNameAreRequiredFields(response);
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    @Description("В данном сценарии проверяется, что создание пользователя без почты невозможно")
    public void userWithoutEmailCantBeCreated() {
        User user = generator.randomUser();
        user.setEmail(null);
        Response response = UserClient.createUser(user);
        status.emailPasswordAndNameAreRequiredFields(response);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("В данном сценарии проверяется, что создание пользователя без пароля невозможно")
    public void userWithoutPasswordCantBeCreated() {
        User user = generator.randomUser();
        user.setPassword(null);
        Response response = UserClient.createUser(user);
        status.emailPasswordAndNameAreRequiredFields(response);
    }

}