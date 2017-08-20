package csokicraft.forge.gorgecore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerModels(){
		ItemModelMesher imm=Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation mdlLoc=new ModelResourceLocation("gorgecore:wishbone");
		imm.register(GorgeCore.wishbone, 0, mdlLoc);
		ModelBakery.registerItemVariants(GorgeCore.wishbone, mdlLoc);
	}
}
