package csokicraft.forge.gorgecore.editor;

import java.io.IOException;

import csokicraft.forge.gorgecore.GorgeCore;
import csokicraft.forge.gorgecore.recipe.GorgeRecipes;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class ContainerRecipeEditor extends Container{
	protected EntityPlayer owner;
	protected String name;
	protected boolean add;
	protected InventoryRecipeEditor inv;
	
	public ContainerRecipeEditor(EntityPlayer p, String s, boolean b){
		owner=p;
		name=s;
		add=b;
		addPlayerSlots(p.inventory);
		inv=new InventoryRecipeEditor(name);
		addSlotToContainer(new Slot(inv, 0,  56, 35));
		addSlotToContainer(new Slot(inv, 1, 116, 35));
		
		if(!add){
			inv.setRecipe(GorgeRecipes.inst.getRecipe(name));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return owner.equals(playerIn);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn){
		if(playerIn.getEntityWorld().isRemote)
			return;
		ItemStack in=inv.getStackInSlot(0);
		ItemStack out=inv.getStackInSlot(1);
		
		if(in==null||out==null){
			playerIn.addChatComponentMessage(new TextComponentString(I18n.format("commands.gorgecore.empty", name)));
			return;
		}
		
		if(!add)
			GorgeRecipes.inst.remove(GorgeRecipes.inst.getRecipe(name));
		try {
			GorgeRecipes.inst.register(in, out, name);
			GorgeCore.inst.saveRecipes();
			playerIn.addChatComponentMessage(new TextComponentString(I18n.format("commands.gorgecore.saved", name)));
		} catch (Exception e){
			playerIn.addChatComponentMessage(new TextComponentString(I18n.format("commands.gorgecore.error", name)));
			e.printStackTrace();
		}
	}
	
	/** From ContainerFurnace */
	protected void addPlayerSlots(InventoryPlayer ip){
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(ip, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(ip, i, 8 + i * 18, 142));
		}
	}
}
