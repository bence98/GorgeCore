package csokicraft.forge.gorgecore;

import java.util.HashMap;
import java.util.Map;

import csokicraft.forge.gorgecore.editor.ContainerRecipeEditor;
import csokicraft.forge.gorgecore.editor.GuiRecipeEditor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{
	public static final int GUIID_REC_ADD=0, GUIID_REC_EDIT=1;
	
	public void registerModels(){}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case GUIID_REC_ADD:
			return new ContainerRecipeEditor(player, get(x), true);
		case GUIID_REC_EDIT:
			return new ContainerRecipeEditor(player, get(x), false);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case GUIID_REC_ADD:
			return new GuiRecipeEditor(player, get(x), true);
		case GUIID_REC_EDIT:
			return new GuiRecipeEditor(player, get(x), false);
		}
		return null;
	}
	
	protected static Map<Integer, String> recipeNames=new HashMap<>();
	
	public static int add(String s){
		int i=0;
		while(recipeNames.containsKey(i))
			i++;
		recipeNames.put(i, s);
		return i;
	}
	
	public static String get(int i){
		return recipeNames.get(i);
	}
	
	public static void remove(int i){
		recipeNames.remove(i);
	}
}
