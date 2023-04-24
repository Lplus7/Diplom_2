package ingredient;

import java.util.List;

public class Ingredients {
    private boolean result;
    private List<Ingredient> data;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean success) {
        this.result = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    public Ingredient getIngredientWithName(String name) {
        return getData().stream()
                .filter(ingredient -> ingredient.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
