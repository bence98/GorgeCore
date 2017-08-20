package csokicraft.forge.gorgecore.item;

import csokicraft.util.mcforge.UtilMcForge10;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemWishbone extends Item {
	public ItemWishbone(){
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(!worldIn.isRemote){
			itemStackIn.stackSize--;
			
			int x=worldIn.rand.nextInt(100);
			if(x<25){
				sendChatTranslated(playerIn, "chat.gorgecore.bonemeal");
				UtilMcForge10.giveItemStack(playerIn, new ItemStack(Items.DYE, 1, 15));
			}else if(x<65){
				sendChatTranslated(playerIn, "chat.gorgecore.nothing");
			}else if(x<90){
				sendChatTranslated(playerIn, "chat.gorgecore.damage");
				playerIn.attackEntityFrom(DamageSource.cactus, 2);
			}else{
				sendChatTranslated(playerIn, "chat.gorgecore.gold");
				UtilMcForge10.giveItemStack(playerIn, new ItemStack(Items.GOLD_INGOT));
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	private static void sendChatTranslated(EntityPlayer p, String s){
		p.addChatComponentMessage(new TextComponentString(I18n.format(s)));
	}
}
