package net.sashakyotoz.anitexlib.api.common.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public interface IEntityTickItem {
    void onTick(Level level, ItemEntity entity);
}