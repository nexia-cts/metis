package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.item.container.menu.Button;
import com.combatreforged.metis.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.metis.api.world.item.container.menu.SlotClickType;
import org.jetbrains.annotations.Nullable;

/**
 * Calls when a player clicks on an inventory slot.
 */
public class PlayerContainerClickEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerContainerClickEvent> BACKEND = EventBackend.create(PlayerContainerClickEvent.class);

    private boolean cancelled;

    private final ContainerMenu menu;
    private final int targetSlot; // -1 for Button.DROP, -999 if no slot is selected (depends on type)
    private final SlotClickType slotClickType;
    private final Button button;
    @Nullable private final ItemStack targetStack;
    @Nullable private final ItemStack cursorStack;

    public PlayerContainerClickEvent(Player player, ContainerMenu menu, int targetSlot, SlotClickType slotClickType, Button button, @Nullable ItemStack targetStack, @Nullable ItemStack cursorStack) {
        super(player);
        this.menu = menu;
        this.targetSlot = targetSlot;
        this.slotClickType = slotClickType;
        this.button = button;
        this.targetStack = targetStack;
        this.cursorStack = cursorStack;
    }

    /**
     * Returns the menu the player has open.
     *
     * @return the menu the player has open
     */
    public ContainerMenu getMenu() {
        return menu;
    }

    /**
     * Returns the slot on which the player is clicking.
     *
     * @return the slot on which the player is clicking (-999 if no slot is selected, -1 for Button.DROP)
     */
    public int getTargetSlot() {
        return targetSlot;
    }

    /**
     * Returns the clicktype.
     *
     * @return the clicktype
     */
    public SlotClickType getClickType() {
        return slotClickType;
    }

    /**
     * Returns the button the player is using.
     *
     * @return the button the player is using
     */
    public Button getButton() {
        return button;
    }

    /**
     * Returns the itemstack on which the player is clicking.
     *
     * @return the itemstack on which the player is clicking
     */
    @Nullable public ItemStack getTargetStack() {
        return targetStack;
    }

    /**
     * Returns the itemstack which is on the mouse.
     *
     * @return the itemstack which is on the mouse
     */
    @Nullable public ItemStack getCursorStack() {
        return cursorStack;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
