package net.sashakyotoz.anitexlib.utils;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;

public class ExampleItem extends Item implements IParticleItem {
    private static final RandomSource random = RandomSource.create();

    public ExampleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void addParticles(Level level, ItemEntity entity) {
        if (random.nextFloat() < 0.1)
            level.addParticle(ModParticleTypes.SPARK_LIKE_PARTICLE.get(), entity.getX() + ((random.nextDouble() - 0.5D) * 0.25), entity.getY() + 0.25F + ((random.nextDouble() - 0.5D) * 0.25), entity.getZ() + ((random.nextDouble() - 0.5D) * 0.25), 1, 1, 1);
    }
}