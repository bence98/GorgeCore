package csokicraft.forge.gorgecore.recipe;

import java.util.*;

import net.minecraft.item.ItemStack;

public class GorgeRecipes{
	protected List<GorgeRecipe> recipes=new LinkedList<>();
	protected Map<String, GorgeRecipe> namedRecipes=new HashMap<>();
	
	public static final GorgeRecipes inst=new GorgeRecipes();
	
	public void register(ItemStack in, ItemStack out){
		if(in==null||out==null)
			throw new NullPointerException("GorgeCore couldn't load recipe: input/output was null!");
		if(getRecipe(in)!=null)
			throw new RuntimeException("GorgeCore couldn't load recipe: Duplicate input: "+in);
		add(new GorgeRecipe(in, out));
	}
	
	public void register(ItemStack in, ItemStack out, String s){
		if(in==null||out==null)
			throw new NullPointerException("GorgeCore couldn't load recipe: input/output was null!");
		if(getRecipe(in)!=null)
			throw new RuntimeException("GorgeCore couldn't load recipe: Duplicate input: "+in);
		add(new GorgeRecipe(in, out, s));
	}
	
	public void add(GorgeRecipe rec){
		if(getRecipe(rec.getName())!=null){
			System.out.println("[WARN] Duplicate recipe name: "+rec.name);
			//rec.name+="_";
			//add(rec);
		}else{
			recipes.add(rec);
			namedRecipes.put(rec.getName(), rec);
		}
	}
	
	public GorgeRecipe getRecipe(ItemStack in){
		for(GorgeRecipe rec:recipes){
			if(rec.isApplicable(in))
				return rec;
		}
		return null;
	}
	
	public GorgeRecipe getRecipe(String s){
		return namedRecipes.get(s);
	}
	
	public Set<String> getNames(){
		return namedRecipes.keySet();
	}
	
	public void remove(GorgeRecipe rec){
		recipes.remove(rec);
		namedRecipes.remove(rec.name);
	}
	
	public void clear(){
		recipes.clear();
	}
}
