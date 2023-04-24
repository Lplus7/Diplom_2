package user;

import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserStatuses {

    public void userAlreadyExists(Response response) {
        response.then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body(" message", equalTo("User already exists"));
    }

    public void emailPasswordAndNameAreRequiredFields(Response response) {
        response.then().assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    public void loggedInSuccessfully(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    public void emailOrPasswordAreIncorrect(Response response) {
        response.then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    public void createdSuccessfully(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    public void youShouldBeAuthorised(Response response) {
        response.then().assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
