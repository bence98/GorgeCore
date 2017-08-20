package csokicraft.forge.gorgecore.editor;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRecipeEditor extends GuiContainer{
	public GuiRecipeEditor(EntityPlayer p, String s, boolean b){
		super(new ContainerRecipeEditor(p, s, b));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("gorgecore:textures/gui/editor.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
