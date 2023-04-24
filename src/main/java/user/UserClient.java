package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post( "auth/register");
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(UserResponse userResponse) {
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", userResponse.getToken())
                .when()
                .delete("auth/user");
    }

    @Step("Логин пользователя")
    public static Response loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post("auth/login");
    }

    @Step("Редактирование пользователя после авторизации")
    public static Response editUserWithToken(UserResponse userResponse, User user) {
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", userResponse.getToken())
                .body(user)
                .when()
                .patch("auth/user");
    }

    @Step("Редактирование пользователя без авторизации")
    public static Response editUserWithoutToken(User user) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .patch("auth/user");
    }


}
