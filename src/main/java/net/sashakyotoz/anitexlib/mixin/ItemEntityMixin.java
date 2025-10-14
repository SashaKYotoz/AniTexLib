package net.sashakyotoz.anitexlib.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Unique
    public ItemEntity aniTexLib$copy = ((ItemEntity) (Object) this);

    @Inject(method = "tick", at = @At("RETURN"))
    public void addParticles(CallbackInfo ci) {
        if (aniTexLib$copy != null) {
            if (aniTexLib$copy.getItem().getItem() instanceof IParticleItem item) {
                if (aniTexLib$copy.level().isClientSide())
                    item.addParticles(aniTexLib$copy.level(), aniTexLib$copy);
                if (!aniTexLib$copy.level().isClientSide())
                    item.addParticles(aniTexLib$copy.level(), (ItemEntity) (Object) this);
            }
        }
    }

    @Inject(method = "playerTouch", at = @At("RETURN"))
    public void playerTouch(Player pEntity, CallbackInfo ci) {
        if (aniTexLib$copy != null) {
            if (aniTexLib$copy.getItem().getItem() instanceof IParticleItem item)
                item.onPlayerTouch(pEntity, aniTexLib$copy);
        }
    }
}