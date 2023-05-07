package client;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import io.restassured.builder.RequestSpecBuilder;

public class Client {

    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
