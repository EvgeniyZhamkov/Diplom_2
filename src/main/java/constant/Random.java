package constant;

import client.Client;
import client.ClientEditedData;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;

public class Random {
    static Faker faker = new Faker();

    public static Client generateUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6, 10);
        String name = faker.name().fullName();
        return new Client(email, password, name);
    }

    public static ClientEditedData generateUserEditedData() {
        String newEmail = faker.internet().emailAddress();
        String newPassword = faker.internet().password(6, 10);
        String newName = faker.name().fullName();
        return new ClientEditedData(newEmail, newPassword, newName);
    }

    public static int generateSizeForIngredientSublist(int max) {
        int sublistIndex = RandomUtils.nextInt(1, max);
        return sublistIndex;
    }
}


