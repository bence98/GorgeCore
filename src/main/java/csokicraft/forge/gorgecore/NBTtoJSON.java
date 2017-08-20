package csokicraft.forge.gorgecore;

import java.util.Map.Entry;

import com.google.gson.*;

import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTtoJSON{
	public static JsonObject fromNBT(NBTTagCompound c){
		JsonObject obj=new JsonObject();
		for(String s:c.getKeySet()){
			obj.add(s, fromNBT(c.getTag(s)));
		}
		return obj;
	}
	
	public static JsonArray fromNBT(NBTTagList nbt){
		JsonArray arr=new JsonArray();
		for(int i=0;i<nbt.tagCount();i++){
			arr.add(fromNBT(nbt.get(i)));
		}
		return arr;
	}
	
	public static JsonElement fromNBT(NBTBase tag){
		switch(tag.getId()){
		case NBT.TAG_BYTE:
			return new JsonPrimitive(((NBTTagByte)tag).getByte());
		case NBT.TAG_INT:
			return new JsonPrimitive(((NBTTagInt)tag).getInt());
		case NBT.TAG_SHORT:
			return new JsonPrimitive(((NBTTagShort)tag).getShort());
		case NBT.TAG_LONG:
			return new JsonPrimitive(((NBTTagLong)tag).getLong());
		case NBT.TAG_FLOAT:
			return new JsonPrimitive(((NBTTagFloat)tag).getFloat());
		case NBT.TAG_DOUBLE:
			return new JsonPrimitive(((NBTTagDouble)tag).getDouble());
		case NBT.TAG_STRING:
			return new JsonPrimitive(((NBTTagString)tag).getString());
		case NBT.TAG_COMPOUND:
			return fromNBT((NBTTagCompound)tag);
		case NBT.TAG_LIST:
			return fromNBT((NBTTagList)tag);
		}
		return null;
	}
	
	public static NBTBase toNBT(JsonElement el){
		if(el.isJsonPrimitive()){
			JsonPrimitive pr=el.getAsJsonPrimitive();
			if(pr.isBoolean())
				return new NBTTagByte((byte) (pr.getAsBoolean()?1:0));
			if(pr.isString())
				return new NBTTagString(pr.getAsString());
			if(pr.isNumber()){
				return new NBTTagDouble(pr.getAsNumber().doubleValue());
			}
			
		}else if(el.isJsonArray()){
			return toNBT(el.getAsJsonArray());
		}else if(el.isJsonObject()){
			return toNBT(el.getAsJsonObject());
		}
		return null;
	}
	
	public static NBTTagCompound toNBT(JsonObject obj){
		NBTTagCompound c=new NBTTagCompound();
		for(Entry<String, JsonElement> e:obj.entrySet()){
			c.setTag(e.getKey(), toNBT(e.getValue()));
		}
		return c;
	}
	
	public static NBTTagList toNBT(JsonArray arr){
		NBTTagList l=new NBTTagList();
		for(JsonElement tmp:arr){
			l.appendTag(toNBT(tmp));
		}
		return l;
	}
}
