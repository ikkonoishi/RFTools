package mcjty.rftools.blocks;

import mcjty.lib.container.GenericBlock;
import mcjty.lib.container.GenericItemBlock;
import mcjty.lib.entity.GenericTileEntity;
import mcjty.lib.varia.GlobalCoordinate;
import mcjty.lib.varia.Logging;
import mcjty.rftools.RFTools;
import mcjty.rftools.blocks.blockprotector.BlockProtectors;
import mcjty.rftools.blocks.security.OrphaningCardItem;
import mcjty.rftools.blocks.security.SecurityChannels;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collection;

public abstract class GenericRFToolsBlock<T extends GenericTileEntity, C extends Container> extends GenericBlock<T, C> {

    public GenericRFToolsBlock(Material material,
                               Class<? extends T> tileEntityClass,
                               Class<? extends C> containerClass,
                               String name, boolean isContainer) {
        super(RFTools.instance, material, tileEntityClass, containerClass, GenericItemBlock.class, name, isContainer);
        setCreativeTab(RFTools.tabRfTools);
    }

    public GenericRFToolsBlock(Material material,
                               Class<? extends T> tileEntityClass,
                               Class<? extends C> containerClass,
                               Class<? extends ItemBlock> itemBlockClass,
                               String name, boolean isContainer) {
        super(RFTools.instance, material, tileEntityClass, containerClass, itemBlockClass, name, isContainer);
        setCreativeTab(RFTools.tabRfTools);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        boolean b = super.canEntityDestroy(state, world, pos, entity);
        if (b) {
            Collection<GlobalCoordinate> protectors = BlockProtectors.getProtectors(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
            if (BlockProtectors.checkHarvestProtection(pos.getX(), pos.getY(), pos.getZ(), world, protectors)) {
                return false;
            }
        }
        return b;
    }

    @Override
    protected boolean checkAccess(World world, EntityPlayer player, TileEntity te) {
        if (te instanceof GenericTileEntity) {
            GenericTileEntity genericTileEntity = (GenericTileEntity) te;
            if ((!OrphaningCardItem.isPrivileged(player, world)) && (!player.getPersistentID().equals(genericTileEntity.getOwnerUUID()))) {
                int securityChannel = genericTileEntity.getSecurityChannel();
                if (securityChannel != -1) {
                    SecurityChannels securityChannels = SecurityChannels.getChannels(world);
                    SecurityChannels.SecurityChannel channel = securityChannels.getChannel(securityChannel);
                    boolean playerListed = channel.getPlayers().contains(player.getDisplayNameString());
                    if (channel.isWhitelist() != playerListed) {
                        Logging.message(player, TextFormatting.RED + "You have no permission to use this block!");
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
