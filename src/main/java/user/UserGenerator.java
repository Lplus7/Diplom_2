package user;


import com.github.javafaker.Faker;
import java.util.Locale;


public class UserGenerator {
    static Faker faker = new Faker();
    static Faker fakerRu = new Faker(new Locale("ru"));

    public User randomUser() {
        return new User(faker.name().username() + "@yandex.ru", faker.internet().password(6, 8,
                true, false, true), fakerRu.name().name());
    }

    public static String generateEmail() {
        return String.format(faker.name().username() + "@yandex.ru");
    }

    public static String generatePassword() {
        return String.format(faker.internet().password(6, 8,
                true, false, true));
    }

    public static String generateName() {
        return String.format(fakerRu.name().name());
    }


}
