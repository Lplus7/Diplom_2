package order;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import user.UserResponse;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public static Response getOrdersWithAuthorization(UserResponse userResponse) {
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", userResponse.getToken())
                .when()
                .get("orders");
    }

    public static Response getOrdersWithoutAuthorization() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("orders");
    }

    public static Response createOrderWithAuthorization(UserResponse userResponse, Order order) {
        if (order != null) {
            return given()
                    .contentType(ContentType.JSON)
                    .header("authorization", userResponse.getToken())
                    .and()
                    .body(order)
                    .when()
                    .post("orders");
        }
        return given()
                .contentType(ContentType.JSON)
                .header("authorization", userResponse.getToken())
                .when()
                .post("orders");
    }

    public static Response createOrderWithoutAuthorization(Order order) {
        if (order != null) {
            return given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(order)
                    .when()
                    .post("orders");
        }
        return given()
                .contentType(ContentType.JSON)
                .when()
                .post("orders");
    }
}
