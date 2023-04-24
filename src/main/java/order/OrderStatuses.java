package order;

import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;

public class OrderStatuses {
    public void createdSuccessfully(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }
}
