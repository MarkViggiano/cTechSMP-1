package me.mark.techsmp.util;

import me.mark.techsmp.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftUtil {

    public static ShapedRecipe createRecipe(ItemStack result, Material topLeft, Material topMiddle, Material topRight,
                                     Material middleLeft, Material middle, Material middleRight,
                                     Material bottomLeft, Material bottomMiddle, Material bottomRight) {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape("123", "456", "789");
        recipe.setIngredient('1', topLeft);
        recipe.setIngredient('2', topMiddle);
        recipe.setIngredient('3', topRight);
        recipe.setIngredient('4', middleLeft);
        recipe.setIngredient('5', middle);
        recipe.setIngredient('6', middleRight);
        recipe.setIngredient('7', bottomLeft);
        recipe.setIngredient('8', bottomMiddle);
        recipe.setIngredient('9', bottomRight);
        Main.getInstance().getServer().addRecipe(recipe);
        return recipe;
    }


}
