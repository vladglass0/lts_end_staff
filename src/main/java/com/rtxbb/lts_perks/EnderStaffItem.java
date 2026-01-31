package com.rtxbb.lts_perks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnderStaffItem extends Item {
    public EnderStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Raycast to find where the player is looking (max distance 50 blocks)
        BlockHitResult ray = level.clip(new ClipContext(
                player.getEyePosition(1.0F),
                player.getEyePosition(1.0F).add(player.getViewVector(1.0F).scale(50.0D)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        if (ray.getType() != HitResult.Type.MISS) {
            BlockPos targetPos = ray.getBlockPos();
            // Adjust to teleport slightly above the block or at the face hit
            Vec3 teleportVec = ray.getLocation();
            
            if (!level.isClientSide) {
                // Teleport logic
                player.teleportTo(teleportVec.x, teleportVec.y, teleportVec.z);
                player.fallDistance = 0;
                
                // Sound effects
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                
                // Cooldown (2 seconds = 40 ticks)
                player.getCooldowns().addCooldown(this, 40);
            } else {
                // Particles on client side
                for (int i = 0; i < 32; ++i) {
                    level.addParticle(ParticleTypes.PORTAL, player.getX(), player.getY() + level.random.nextDouble() * 2.0D, player.getZ(), level.random.nextGaussian(), 0.0D, level.random.nextGaussian());
                }
            }

            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }

        return InteractionResultHolder.pass(itemstack);
    }
}
