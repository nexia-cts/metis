package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.metis.api.world.damage.DamageData;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.scoreboard.ScoreboardTeam;
import net.kyori.adventure.text.Component;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends LivingEntityDeathEvent {
    public static final EventBackend<PlayerDeathEvent> BACKEND = EventBackend.create(PlayerDeathEvent.class);

    private final Player player;
    private Component deathMessage;
    private ScoreboardTeam.VisibleFor visibleFor;

    public PlayerDeathEvent(Player player, DamageData cause, boolean dropLoot, boolean dropEquipment, boolean dropExperience, Component deathMessage, ScoreboardTeam.VisibleFor visibleFor) {
        super(player, cause, dropLoot, dropEquipment, dropExperience);
        this.player = player;
        this.deathMessage = deathMessage;
        this.visibleFor = visibleFor;
    }

    /**
     * Returns the Player involved in this event.
     *
     * @return Player who is involved in this event.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     */
    public Component getDeathMessage() {
        return deathMessage;
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    public void setDeathMessage(Component deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Gets the scoreboard team whos players can see the death message.
     *
     * @return scoreboard team whos players can see the death message.
     */
    public ScoreboardTeam.VisibleFor getVisibleFor() {
        return visibleFor;
    }

    /**
     * Sets the scoreboard team whos players should see the death message.
     *
     * @param visibleFor scoreboard team whos players should see the death message.
     */
    public void setVisibleFor(ScoreboardTeam.VisibleFor visibleFor) {
        this.visibleFor = visibleFor;
    }
}
