package csokicraft.forge.gorgecore.editor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import csokicraft.forge.gorgecore.CommonProxy;
import csokicraft.forge.gorgecore.GorgeCore;
import csokicraft.forge.gorgecore.recipe.GorgeRecipe;
import csokicraft.forge.gorgecore.recipe.GorgeRecipes;
import csokicraft.util.CollectionUtils;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandGorgeCore extends CommandBase{

	@Override
	public int compareTo(ICommand arg0){
		return arg0.getCommandName().compareTo(getCommandName());
	}

	@Override
	public String getCommandName(){
		return "gorgecore";
	}

	@Override
	public String getCommandUsage(ICommandSender sender){
		return "gorgecore (add|edit|delete <recipe_name>) | (save|reload)";
	}

    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
		EntityPlayer p=getCommandSenderAsPlayer(sender);
		switch(args[0]){
		case "add":
			if(args.length<2)
				throw new WrongUsageException(getCommandUsage(sender));
			addRecipe(sender, p, args[1]);
			break;
		case "edit":
			if(args.length<2)
				throw new WrongUsageException(getCommandUsage(sender));
			editRecipe(sender, p, args[1]);
			break;
		case "delete":
			if(args.length<2)
				throw new WrongUsageException(getCommandUsage(sender));
			deleteRecipe(sender, p, args[1]);
			break;
		case "save":
			try {
				GorgeCore.inst.saveRecipes();
				notifyCommandListener(sender, this, "commands.gorgecore.save_done");
			} catch (IOException e){
				e.printStackTrace();
				throw new CommandException("commands.gorgecore.save_error");
			}
			break;
		case "reload":
			try {
				GorgeCore.inst.reloadRecipes();
				notifyCommandListener(sender, this, "commands.gorgecore.load_done");
			} catch (IOException e){
				e.printStackTrace();
				throw new CommandException("commands.gorgecore.load_error");
			}
			break;
		}
	}

	private void deleteRecipe(ICommandSender sender, EntityPlayer p, String name) throws CommandException{
		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(name);
		if(rec==null)
			throw new CommandException("commands.gorgecore.norecipe", name);
		
		GorgeCore.inst.removeRecipe(name);
		notifyCommandListener(sender, this, "commands.gorgecore.removed", name);
	}

	private void editRecipe(ICommandSender sender, EntityPlayer p, String name) throws CommandException{
		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(name);
		if(rec==null)
			throw new CommandException("commands.gorgecore.norecipe", name);
		
		int x=CommonProxy.add(name);
		p.openGui(GorgeCore.inst, CommonProxy.GUIID_REC_EDIT, p.getEntityWorld(), x, 0, 0);
	}

	private void addRecipe(ICommandSender sender, EntityPlayer p, String name) throws CommandException{
		GorgeRecipe rec=GorgeRecipes.inst.getRecipe(name);
		if(rec!=null)
			throw new CommandException("commands.gorgecore.duplicate", name);
		
		int x=CommonProxy.add(name);
		p.openGui(GorgeCore.inst, CommonProxy.GUIID_REC_ADD, p.getEntityWorld(), x, 0, 0);
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos){
		if(args.length==1){
			if(args[0].length()==0){
				return CollectionUtils.fromArray(new String[]{"add", "edit", "delete"});
			}
			switch(args[0].charAt(0)){
			case 'a':
				return Collections.singletonList("add");
			case 'e':
				return Collections.singletonList("edit");
			case 'd':
				return Collections.singletonList("delete");
			case 's':
				return Collections.singletonList("save");
			case 'r':
				return Collections.singletonList("reload");
			}
		}else if(args.length==2&&("edit".equals(args[0])||"delete".equals(args[0]))){
			Set<String> names=GorgeRecipes.inst.getNames();
			return names.stream().filter(x -> (x.startsWith(args[1]))).collect(Collectors.toList());
		}
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
