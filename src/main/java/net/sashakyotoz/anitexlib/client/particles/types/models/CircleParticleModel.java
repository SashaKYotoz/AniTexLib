package net.sashakyotoz.anitexlib.client.particles.types.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.sashakyotoz.anitexlib.AniTexLib;

public class CircleParticleModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AniTexLib.MODID, "circle_particle_model"), "main");
    private final ModelPart cycle;

    public CircleParticleModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.cycle = root.getChild("cycle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition cycle = partdefinition.addOrReplaceChild("cycle", CubeListBuilder.create().texOffs(-32, 0).addBox(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        cycle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}