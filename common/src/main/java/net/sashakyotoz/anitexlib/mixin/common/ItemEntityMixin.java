package net.sashakyotoz.anitexlib.mixin.common;

import net.minecraft.world.entity.item.ItemEntity;
import net.sashakyotoz.anitexlib.api.common.item.IEntityTickItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Unique
    public ItemEntity aniTexLib$copy = ((ItemEntity) (Object) this);

    @Inject(at = @At("RETURN"), method = "tick")
    public void addParticles(CallbackInfo ci) {
        if (aniTexLib$copy != null) {
            if (aniTexLib$copy.getItem().getItem() instanceof IEntityTickItem item)
                    item.onTick(aniTexLib$copy.level(), aniTexLib$copy);
        }
    }
}