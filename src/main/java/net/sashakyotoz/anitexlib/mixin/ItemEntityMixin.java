package net.sashakyotoz.anitexlib.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "tick", at = @At("RETURN"))
    public void addParticles(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) ((Object) this);
        if (entity != null) {
            if (entity.getItem().getItem() instanceof IParticleItem item) {
                if (entity.level().isClientSide())
                    item.addParticles(entity.level(), entity);
                else
                    item.serverTick(entity.level(), entity);
            }
        }
    }

    @Inject(method = "playerTouch", at = @At("RETURN"))
    public void playerTouch(Player pEntity, CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) ((Object) this);
        if (entity != null) {
            if (entity.getItem().getItem() instanceof IParticleItem item)
                item.onPlayerTouch(pEntity, entity);
        }
    }
}