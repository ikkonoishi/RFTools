package com.mcjty.rftools.blocks.dimlets;

import com.mcjty.entity.GenericEnergyHandlerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class DimletResearcherTileEntity extends GenericEnergyHandlerTileEntity implements ISidedInventory {

    private ItemStack stacks[] = new ItemStack[2];

    public DimletResearcherTileEntity() {
        super(DimletConfiguration.RESEARCHER_MAXENERGY, DimletConfiguration.RESEARCHER_RECEIVEPERTICK);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return DimletResearcherContainerFactory.getInstance().getAccessibleSlots();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack item, int side) {
        return DimletResearcherContainerFactory.getInstance().isInputSlot(index);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack item, int side) {
        return DimletResearcherContainerFactory.getInstance().isOutputSlot(index);
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return stacks[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (stacks[index] != null) {
            if (stacks[index].stackSize <= amount) {
                ItemStack old = stacks[index];
                stacks[index] = null;
                markDirty();
                return old;
            }
            ItemStack its = stacks[index].splitStack(amount);
            if (stacks[index].stackSize == 0) {
                stacks[index] = null;
            }
            markDirty();
            return its;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        stacks[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Researcher Inventory";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
}
