package client;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;
import usermodel.User;
import usermodel.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String USER_REGISTER = "auth/register";
    private static final String USER_LOGIN = "auth/login";
    private static final String USER = "auth/user";

    @Step("Создание пользователя {user}")
    public ValidatableResponse makeUser (User user) {
        return given()
                .spec(getSpec())
                .body(user).log().all()
                .when()
                .post(USER_REGISTER)
                .then().log().all();
    }

    @Step("Авторизация {userCredentials}")
    public ValidatableResponse loginUser (UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .body(userCredentials).log().all()
                .when()
                .post(USER_LOGIN)
                .then().log().all();
    }

    @Step("Получение информации о пользователе {token}")
    public ValidatableResponse getUserInfo (String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token).log().all()
                .when()
                .get(USER)
                .then().log().all();
    }

    @Step("Редактирование пользователя {user}")
    public ValidatableResponse editUserWithLogin (User user, String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .body(user).log().all()
                .when()
                .patch(USER)
                .then().log().all();
    }

    @Step("Редактирование пользователя без авторизации {user}")
    public ValidatableResponse editUserWithoutLogin (User user) {
        return given()
                .spec(getSpec())
                .body(user).log().all()
                .when()
                .patch(USER)
                .then().log().all();
    }


    @Step("Удаление пользователя {user}")
    public ValidatableResponse deleteUser (String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token).log().all()
                .when()
                .delete(USER)
                .then().log().all();
    }

}
