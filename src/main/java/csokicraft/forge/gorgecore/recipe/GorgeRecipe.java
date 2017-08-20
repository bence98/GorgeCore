package csokicraft.forge.gorgecore.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;

public class GorgeRecipe{
	protected ItemStack in, out;
	protected String name;
	
	public GorgeRecipe(ItemStack input, ItemStack output, String s){
		in=input;
		out=output;
		name=s;
	}
	
	public GorgeRecipe(ItemStack input, ItemStack output){
		this(input, output, Long.toHexString(new Random().nextLong()));
	}
	
	public boolean isApplicable(ItemStack is){
		return ItemStack.areItemsEqual(in, is)&&ItemStack.areItemStackTagsEqual(in, is);
	}
	
	/** Creates new stack */
	public ItemStack getOutput(){
		return out.copy();
	}
	
	/** Doesn't create new stack, don't modify! */
	public ItemStack getInput(){
		return in;
	}
	
	public String getName(){
		return name;
	}
}