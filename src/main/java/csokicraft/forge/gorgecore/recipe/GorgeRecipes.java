package csokicraft.forge.gorgecore.recipe;

import java.util.*;

import net.minecraft.item.ItemStack;

public class GorgeRecipes{
	protected List<GorgeRecipe> recipes=new LinkedList<>();
	
	public static final GorgeRecipes inst=new GorgeRecipes();
	
	public void register(ItemStack in, ItemStack out){
		if(getRecipe(in)!=null)
			throw new RuntimeException("GorgeCore couldn't load recipe: Duplicate input: "+in);
		recipes.add(new GorgeRecipe(in, out));
	}
	
	public GorgeRecipe getRecipe(ItemStack in){
		for(GorgeRecipe rec:recipes){
			if(rec.isApplicable(in))
				return rec;
		}
		return null;
	}
	
	public void remove(GorgeRecipe rec){
		recipes.remove(rec);
	}
}
