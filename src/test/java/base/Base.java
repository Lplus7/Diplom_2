package base;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import user.UserResponse;
import user.UserClient;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.core.IsEqual.equalTo;

public class Base {

    public UserResponse userResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
    }

    @After
    public void afterTest() {
        if (userResponse != null) {
            UserClient.deleteUser(userResponse)
                    .then().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }

}
