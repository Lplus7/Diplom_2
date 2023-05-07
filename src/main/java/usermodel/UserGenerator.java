package usermodel;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandomUser() {
        String email = RandomStringUtils.randomAlphanumeric(12) + "@bravo.com";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }

    public static User getUserWithoutEmail() {
        String email = "";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }

    public static User getUserWithoutName() {
        String email = RandomStringUtils.randomAlphanumeric(12) + "@bravo.com";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = "";
        return new User(email, password, name);
    }

    public static User getUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphanumeric(12) + "@bravo.com";
        String password = "";
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }
}
