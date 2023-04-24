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

public class LoginUserTests extends Base{
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
    @DisplayName("Логин с правильными почтой и паролем")
    @Description("В данном сценарии проверяется, что можно залогиниться под созданым пользователем")
    public void userCanBeLoggedIn() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        response = UserClient.loginUser(user);
        status.loggedInSuccessfully(response);
    }

    @Test
    @DisplayName("Логин с неправильной почтой")
    @Description("В данном сценарии проверяется, что нельзя залогиниться с неправильной почтой")
    public void userCantBeLoggedInWithWrongEmail() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        user.setEmail(UserGenerator.generateEmail());
        response = UserClient.loginUser(user);
        status.emailOrPasswordAreIncorrect(response);
    }

    @Test
    @DisplayName("Логин без почты")
    @Description("В данном сценарии проверяется, что нельзя залогиниться без почты")
    public void userCantBeLoggedInWithoutEmail() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        user.setEmail(null);
        response = UserClient.loginUser(user);
        status.emailOrPasswordAreIncorrect(response);
    }

    @Test
    @DisplayName("Логин с неправильным паролем")
    @Description("В данном сценарии проверяется, что нельзя залогиниться с неправильным паролем")
    public void userCantBeLoggedInWithWrongPassword() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        user.setPassword(UserGenerator.generatePassword());
        response = UserClient.loginUser(user);
        status.emailOrPasswordAreIncorrect(response);
    }

    @Test
    @DisplayName("Логин без пароля")
    @Description("В данном сценарии проверяется, что нельзя залогиниться без пароля")
    public void userCantBeLoggedInWithoutPassword() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        status.createdSuccessfully(response);
        userResponse = response.as(UserResponse.class);
        user.setPassword(null);
        response = UserClient.loginUser(user);
        status.emailOrPasswordAreIncorrect(response);
    }

}
