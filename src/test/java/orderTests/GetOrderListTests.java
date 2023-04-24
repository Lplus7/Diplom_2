package orderTests;

import base.Base;
import user.*;
import ingredient.*;
import order.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GetOrderListTests extends Base {
    private final UserGenerator generator = new UserGenerator();
    private final UserStatuses status = new UserStatuses();
    private final IngredientStatuses ingredientStatus = new IngredientStatuses();
    private final OrderStatuses orderStatus = new OrderStatuses();

    @Test
    @DisplayName("Получение списка заказов после авторизации ")
    @Description("В данном сценарии проверяется, что авторизованный пользователь может получить список заказов конкретного пользователя")
    public void getOrderListAfterAuthorization() {
        User user = generator.randomUser();
        Response response = UserClient.createUser(user);
        UserResponse userResponse = response.as(UserResponse.class);
        this.userResponse = userResponse;
        List<String> ingredients = new ArrayList<>();
        response = IngredientClient.getIngredientsList();
        ingredientStatus.ingredientListGotSuccessfully(response);

        Ingredients ingredientsForAdd = response.as(Ingredients.class);
        Ingredient ingredient = ingredientsForAdd.getIngredientWithName("Флюоресцентная булка R2-D3");
        ingredients.add(ingredient.getId());

        Order order = new Order();
        order.setIngredients(ingredients);
        OrderClient.createOrderWithAuthorization(userResponse, order);
        orderStatus.createdSuccessfully(response);

        response = OrderClient.getOrdersWithAuthorization(userResponse);
        status.createdSuccessfully(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации ")
    @Description("В данном сценарии проверяется, что неавторизованный пользователь не может получить список заказов")
    public void getOrderListWithoutAuthorization() {
        Response response = OrderClient.getOrdersWithoutAuthorization();
        status.youShouldBeAuthorised(response);
    }
}
