package csokicraft.forge.gorgecore;

import java.io.File;

import csokicraft.forge.gorgecore.item.ItemWishbone;
import csokicraft.forge.gorgecore.recipe.GorgeRecipe;
import csokicraft.forge.gorgecore.recipe.GorgeRecipes;
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
    public void preinit(FMLPreInitializationEvent event)
    {
        File dir=event.getModConfigurationDirectory();
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.register(wishbone);
        proxy.registerModels();
        addGorgeRecipes();
    }
    
    protected void addGorgeRecipes(){
    	GorgeRecipes.inst.register(new ItemStack(Items.MELON), new ItemStack(Items.MELON_SEEDS));
    	GorgeRecipes.inst.register(new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.MELON_SEEDS));
    	GorgeRecipes.inst.register(new ItemStack(Items.CHICKEN), new ItemStack(wishbone));
    	GorgeRecipes.inst.register(new ItemStack(Items.COOKED_CHICKEN), new ItemStack(wishbone));
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
