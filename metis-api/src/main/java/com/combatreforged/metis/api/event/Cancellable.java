package com.combatreforged.metis.api.event;

public interface Cancellable {
    /**
     * Sets the cancellation state of this event.
     *
     * @param cancelled true if you wish to cancel this event
     */
    void setCancelled(boolean cancelled);

    /**
     * Gets the cancellation state of this event.
     *
     * @return true if this event is cancelled
     */
    boolean isCancelled();
}
