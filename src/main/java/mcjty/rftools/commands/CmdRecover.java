package mcjty.rftools.commands;

import mcjty.rftools.blocks.dimlets.DimletSetup;
import mcjty.rftools.dimension.DimensionInformation;
import mcjty.rftools.dimension.RfToolsDimensionManager;
import mcjty.rftools.dimension.description.DimensionDescriptor;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CmdRecover extends AbstractRfToolsCommand {
    @Override
    public String getHelp() {
        return "[<dimension>]";
    }

    @Override
    public String getCommand() {
        return "recover";
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        if (args.length > 2) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Too many parameters!"));
            return;
        }

        World world = sender.getEntityWorld();
        ItemStack heldItem = null;
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            heldItem = player.getHeldItem();
        }
        if (heldItem == null || heldItem.getItem() != DimletSetup.realizedDimensionTab) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You need to hold a realized dimension tab in your hand!"));
            return;
        }

        int specifiedDim = -1;
        if (args.length == 2) {
            specifiedDim = fetchInt(sender, args, 1, -1);
        }

        NBTTagCompound tagCompound = heldItem.getTagCompound();
        int dim = tagCompound.getInteger("id");
        if ((!tagCompound.hasKey("id")) || dim == 0 || dim == -1) {
            if (specifiedDim == -1) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The dimension id is missing from the tab!"));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You can specify a dimension id manually"));
                return;
            } else {
                dim = specifiedDim;
            }
        }

        RfToolsDimensionManager dimensionManager = RfToolsDimensionManager.getDimensionManager(world);
        DimensionInformation information = dimensionManager.getDimensionInformation(dim);

        if (information != null) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The dimension information is already present!"));
            return;
        }

        if (DimensionManager.isDimensionRegistered(dim)) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This dimension is already registered!"));
            return;
        }


        DimensionDescriptor descriptor = new DimensionDescriptor(tagCompound);
        String name = tagCompound.getString("name");
        dimensionManager.recoverDimension(world, dim, descriptor, name);

        sender.addChatMessage(new ChatComponentText("Dimension was succesfully recovered"));
    }
}
