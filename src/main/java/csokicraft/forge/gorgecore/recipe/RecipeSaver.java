package csokicraft.forge.gorgecore.recipe;

import java.io.*;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import csokicraft.forge.gorgecore.NBTtoJSON;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeSaver implements Closeable{
	JsonWriter jsonWr;
	String name;

	public RecipeSaver(OutputStream os){
		jsonWr=new JsonWriter(new OutputStreamWriter(os));
		name=os.toString();
	}
	
	public RecipeSaver(File f) throws FileNotFoundException{
		this(new FileOutputStream(f));
		name=f.getAbsolutePath();
	}
	
	public void writeRecipe(ItemStack in, ItemStack out) throws IOException{
		NBTTagCompound inNBT=in.writeToNBT(new NBTTagCompound());
		NBTTagCompound outNBT=out.writeToNBT(new NBTTagCompound());
		JsonObject inObj=NBTtoJSON.fromNBT(inNBT);
		JsonObject outObj=NBTtoJSON.fromNBT(outNBT);
		
		jsonWr.beginObject();
		jsonWr.name("input");
		Streams.write(inObj, jsonWr);
		jsonWr.name("output");
		Streams.write(outObj, jsonWr);
		jsonWr.endObject();
	}
	
	@Override
	public void close() throws IOException{
		jsonWr.close();
	}
}
