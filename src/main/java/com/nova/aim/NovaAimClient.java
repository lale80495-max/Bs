package com.nova.aim;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;

public final class NovaAimClient implements ClientModInitializer {
    private static final double RANGE = 6.0;
    private static final float SMOOTHING = 0.12f;
    private static boolean enabled = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            // Hold LEFT ALT to activate. This training build deliberately excludes players.
            boolean holdingAimKey = GLFW.glfwGetKey(
                    client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_ALT
            ) == GLFW.GLFW_PRESS;

            if (!holdingAimKey) return;

            LivingEntity target = client.world.getEntitiesByClass(
                    LivingEntity.class,
                    client.player.getBoundingBox().expand(RANGE),
                    entity -> entity != client.player
                            && entity.isAlive()
                            && !(entity instanceof PlayerEntity)
            ).stream()
             .filter(entity -> client.player.squaredDistanceTo(entity) <= RANGE * RANGE)
             .min(Comparator.comparingDouble(entity ->
                     client.player.squaredDistanceTo(entity)))
             .orElse(null);

            if (target != null) gentlyTrack(client, target);
        });
    }

    private static void gentlyTrack(MinecraftClient client, LivingEntity target) {
        Vec3d eyes = client.player.getEyePos();
        Vec3d point = target.getBoundingBox().getCenter();
        double dx = point.x - eyes.x;
        double dy = point.y - eyes.y;
        double dz = point.z - eyes.z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float wantedYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float wantedPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontal));

        float yawDelta = MathHelper.wrapDegrees(wantedYaw - client.player.getYaw());
        float pitchDelta = MathHelper.wrapDegrees(wantedPitch - client.player.getPitch());

        client.player.setYaw(client.player.getYaw() + yawDelta * SMOOTHING);
        client.player.setPitch(MathHelper.clamp(
                client.player.getPitch() + pitchDelta * SMOOTHING, -90.0f, 90.0f
        ));
    }
}
