package com.rtxbb.lts_perks;

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

        double distance = Config.TELEPORT_DISTANCE.get();
        BlockHitResult ray = level.clip(new ClipContext(
                player.getEyePosition(1.0F),
                player.getEyePosition(1.0F).add(player.getViewVector(1.0F).scale(distance)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        if (ray.getType() != HitResult.Type.MISS) {
            Vec3 teleportVec = ray.getLocation();
            if (!level.isClientSide) {
                player.teleportTo(teleportVec.x, teleportVec.y, teleportVec.z);
                player.fallDistance = 0;
                player.getCooldowns().addCooldown(this, Config.TELEPORT_COOLDOWN.get());
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }

        return InteractionResultHolder.pass(itemstack);
    }
}
