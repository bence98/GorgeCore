package csokicraft.forge.gorgecore;

import java.io.*;

import csokicraft.forge.gorgecore.item.ItemWishbone;
import csokicraft.forge.gorgecore.recipe.GorgeRecipe;
import csokicraft.forge.gorgecore.recipe.GorgeRecipes;
import csokicraft.forge.gorgecore.recipe.RecipeLoader;
import csokicraft.forge.gorgecore.recipe.RecipeSaver;
import csokicraft.util.mcforge.UtilMcForge10;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = GorgeCore.MODID, version = GorgeCore.VERSION, dependencies="required-after: csokicraftutil@1.3.1")
@EventBusSubscriber
public class GorgeCore
{
    public static final String MODID = "gorgecore";
    public static final String VERSION = "1.0";
    
    @Instance
    public static GorgeCore inst;
    @SidedProxy(serverSide="csokicraft.forge.gorgecore.CommonProxy", clientSide="csokicraft.forge.gorgecore.ClientProxy")
    public static CommonProxy proxy;
    
    public static Item wishbone=new ItemWishbone().setUnlocalizedName("wishbone").setRegistryName("wishbone");
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) throws IOException
    {
        GameRegistry.register(wishbone);
        File dir=new File(event.getModConfigurationDirectory(), "gorgecore-recipes");
        System.out.println(dir.getAbsolutePath());
    	if(!dir.exists()){
    		dir.mkdirs();
    		writeDefaultGorgeRecipes(dir);
    		System.out.println("Written");
    	}
    	for(File f:dir.listFiles()){
    		if(f.isFile()&&f.getName().endsWith(".json")){
    			RecipeLoader loader=new RecipeLoader(f);
    			loader.addRecipe();
    			loader.close();
    		}
    	}
		System.out.println("Loaded");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerModels();
    }
    
    protected void writeDefaultGorgeRecipes(File dir) throws IOException{
    	RecipeSaver saver=new RecipeSaver(new File(dir, "melon.json"));
    	saver.writeRecipe(new ItemStack(Items.MELON), new ItemStack(Items.MELON_SEEDS));
    	saver.close();
    	
    	saver=new RecipeSaver(new File(dir, "sp_melon.json"));
    	saver.writeRecipe(new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.MELON_SEEDS));
    	saver.close();
    	
    	saver=new RecipeSaver(new File(dir, "raw_chicken.json"));
    	saver.writeRecipe(new ItemStack(Items.CHICKEN), new ItemStack(wishbone));
    	saver.close();
    	
    	saver=new RecipeSaver(new File(dir, "cooked_chicken.json"));
    	saver.writeRecipe(new ItemStack(Items.COOKED_CHICKEN), new ItemStack(wishbone));
    	saver.close();
    	
    }
    
    @SubscribeEvent
    public static void onPlayerEats(LivingEntityUseItemEvent.Finish evt){
    	EntityLivingBase ent=evt.getEntityLiving();
    	if(!ent.getEntityWorld().isRemote&&ent instanceof EntityPlayer){
    		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(evt.getItem());
    		if(rec!=null){
    			UtilMcForge10.giveItemStack((EntityPlayer) ent, rec.getOutput());
    		}
    	}
    }
}
