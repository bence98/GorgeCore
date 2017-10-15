package csokicraft.forge.gorgecore.recipe;

import java.io.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import csokicraft.forge.gorgecore.NBTtoJSON;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeLoader implements Closeable{
	JsonParser parser;
	Reader reader;
	String name;

	public RecipeLoader(InputStream in){
		parser=new JsonParser();
		reader=new InputStreamReader(in);
		name=reader.toString();
	}
	
	public RecipeLoader(File f) throws FileNotFoundException{
		this(new FileInputStream(f));
		name=f.getName();
		int x=name.lastIndexOf('.');
		if(x>0)
			name=name.substring(0, x);
	}
	
	public void addRecipe(){
		JsonElement el=parser.parse(reader);
		if(el.isJsonObject()){
			JsonObject obj=el.getAsJsonObject();
			JsonElement inEl=obj.get("input");
			JsonElement outEl=obj.get("output");
			if(inEl==null||outEl==null){
				throw new IllegalStateException(toString()+"->parse failed, missing input/output!");
			}else if(inEl.isJsonObject()&&outEl.isJsonObject()){
				try {
					NBTTagCompound inNbt=NBTtoJSON.toNBT(inEl.getAsJsonObject());
					NBTTagCompound outNbt=NBTtoJSON.toNBT(outEl.getAsJsonObject());
					
					ItemStack inStack=new ItemStack(inNbt);
					ItemStack outStack=new ItemStack(outNbt);
					GorgeRecipes.inst.register(inStack, outStack, name);
				} catch (Exception e){
					throw new IllegalStateException(toString()+"->parse failed, "+e.getMessage(), e);
				}
			}else{
				throw new IllegalStateException(toString()+"->parse failed, input/output isn't JsonObject!");
			}
		}else{
			throw new IllegalStateException(toString()+"->parse failed, root isn't JsonObject!");
		}
	}

	@Override
	public void close() throws IOException{
		parser=null;
		reader.close();
	}
	
	@Override
	public String toString(){
		return "RecipeLoader:"+name;
	}
}
