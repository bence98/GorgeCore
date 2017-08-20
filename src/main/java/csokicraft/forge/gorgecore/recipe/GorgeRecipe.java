package csokicraft.forge.gorgecore.recipe;

import net.minecraft.item.ItemStack;

public class GorgeRecipe{
	protected ItemStack in, out;
	
	public GorgeRecipe(ItemStack input, ItemStack output){
		in=input;
		out=output;
	}
	
	public boolean isApplicable(ItemStack is){
		return ItemStack.areItemsEqual(in, is)&&ItemStack.areItemStackTagsEqual(in, is);
	}
	
	/** Creates new stack */
	public ItemStack getOutput(){
		return out.copy();
	}
}