package csokicraft.forge.gorgecore.editor;

import csokicraft.forge.gorgecore.recipe.GorgeRecipe;
import net.minecraft.inventory.InventoryBasic;

public class InventoryRecipeEditor extends InventoryBasic{

	public InventoryRecipeEditor(String name){
		super(name, true, 2);
	}

	public void setRecipe(GorgeRecipe rec){
		setInventorySlotContents(0, rec.getInput());
		setInventorySlotContents(1, rec.getOutput());
	}

}
