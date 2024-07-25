package net.sashakyotoz.anitexlib.utils;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;

public class ExampleItem extends Item implements IParticleItem {
    private static final RandomSource random = RandomSource.create();

    public ExampleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void addParticles(Level level, ItemEntity entity) {
        ColorableParticleOption option = new ColorableParticleOption("sparkle",0.25f,1,0.25f);
        if (random.nextFloat() < 0.1)
            level.addParticle(option, entity.getX(),entity.getY()+0.25f,entity.getZ(), 0, 0, 0);
    }
}