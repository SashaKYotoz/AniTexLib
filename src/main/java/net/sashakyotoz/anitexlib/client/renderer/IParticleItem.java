package net.sashakyotoz.anitexlib.client.renderer;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IParticleItem {
    void addParticles(Level level, ItemEntity entity);

    default void serverTick(Level level, ItemEntity entity){};

    default void onPlayerTouch(Player pEntity, ItemEntity entity){};
}