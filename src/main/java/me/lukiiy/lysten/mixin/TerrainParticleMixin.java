package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TerrainParticle.class)
public class TerrainParticleMixin {
    @Unique
    private static final List<TagKey<Block>> lysten$gravityTags = List.of(BlockTags.LEAVES, BlockTags.FLOWERS, BlockTags.SMALL_FLOWERS);

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
    private void lysten$lessGravity(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {
        if (!LystenClient.lighterBlockParticles.get() || blockState.getTags().filter(lysten$gravityTags::contains).findFirst().orElse(null) == null) return;

        ((ParticleAccessor) this).setGravity(((ParticleAccessor) this).gravity() * .4f);
    }
}
