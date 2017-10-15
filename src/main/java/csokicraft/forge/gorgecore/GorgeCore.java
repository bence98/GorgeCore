package csokicraft.forge.gorgecore;

import java.io.*;

import csokicraft.forge.gorgecore.editor.CommandGorgeCore;
import csokicraft.forge.gorgecore.item.ItemWishbone;
import csokicraft.forge.gorgecore.recipe.*;
import csokicraft.util.mcforge.UtilMcForge11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = GorgeCore.MODID, version = GorgeCore.VERSION, dependencies="required-after: csokicraftutil@1.3.1")
@EventBusSubscriber
public class GorgeCore
{
    public static final String MODID = "gorgecore";
    public static final String VERSION = "1.1";
    
    @Instance
    public static GorgeCore inst;
    @SidedProxy(serverSide="csokicraft.forge.gorgecore.CommonProxy", clientSide="csokicraft.forge.gorgecore.ClientProxy")
    public static CommonProxy proxy;
    
    public static Item wishbone=new ItemWishbone().setUnlocalizedName("wishbone").setRegistryName("wishbone");
    
    protected static File cfgDir;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        cfgDir=new File(event.getModConfigurationDirectory(), "gorgecore-recipes");
        System.out.println(cfgDir.getAbsolutePath());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException
    {
        proxy.registerModels();
        NetworkRegistry.INSTANCE.registerGuiHandler(inst, proxy);
    	if(!cfgDir.exists()){
    		cfgDir.mkdirs();
    		writeDefaultGorgeRecipes();
    	}else reloadRecipes();
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent evt){
    	evt.registerServerCommand(new CommandGorgeCore());
    }
    
    protected void writeDefaultGorgeRecipes() throws IOException{
    	GorgeRecipes.inst.register(new ItemStack(Items.MELON), new ItemStack(Items.MELON_SEEDS), "melon");
    	GorgeRecipes.inst.register(new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.MELON_SEEDS), "sp_melon");
    	GorgeRecipes.inst.register(new ItemStack(Items.COOKED_CHICKEN), new ItemStack(wishbone), "cooked_chicken");
    	GorgeRecipes.inst.register(new ItemStack(Items.CHICKEN), new ItemStack(wishbone), "raw_chicken");
    	
    	saveRecipes();
    }
    
    public void reloadRecipes() throws IOException{
    	GorgeRecipes.inst.clear();
    	for(File f:cfgDir.listFiles()){
    		if(f.isFile()&&f.getName().endsWith(".json")){
    			RecipeLoader loader=new RecipeLoader(f);
    			loader.addRecipe();
    			loader.close();
    		}
    	}
		System.out.println("Loaded");
    }
    
    public void saveRecipes() throws IOException{
    	RecipeSaver saver;
    	for(String s:GorgeRecipes.inst.getNames()){
    		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(s);
    		saver=new RecipeSaver(new File(cfgDir, s+".json"));
    		saver.writeRecipe(rec.getInput(), rec.getOutput());
    		saver.close();
    	}
		System.out.println("Written");
    }
    
    @SubscribeEvent
    public static void onPlayerEats(LivingEntityUseItemEvent.Finish evt){
    	EntityLivingBase ent=evt.getEntityLiving();
    	if(!ent.getEntityWorld().isRemote&&ent instanceof EntityPlayer){
    		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(evt.getItem());
    		if(rec!=null){
    			UtilMcForge11.giveItemStack((EntityPlayer) ent, rec.getOutput());
    		}
    	}
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event){
    	IForgeRegistry<Item> registry=event.getRegistry();
    	registry.register(wishbone);
    }
	
	@SubscribeEvent
	public static void loadModels(ModelRegistryEvent evt){
		proxy.registerModels();
	}

	public void removeRecipe(String name){
		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(name);
		GorgeRecipes.inst.remove(rec);
		File f=new File(cfgDir, name+".json");
		f.delete();
	}
}
