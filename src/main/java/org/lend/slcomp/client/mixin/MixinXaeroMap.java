package org.lend.slcomp.client.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(xaero.map.gui.GuiMap.class)
public class MixinXaeroMap {

    @Shadow
    private Entity player;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        ItemStack main = mc.player.getMainHandStack();
        ItemStack off = mc.player.getOffHandStack();

        boolean hasCompassOrMap =
                main.getItem() instanceof CompassItem ||
                        main.getItem() instanceof FilledMapItem ||
                        off.getItem() instanceof CompassItem ||
                        off.getItem() instanceof FilledMapItem;

        MinecraftClient client = MinecraftClient.getInstance();

        if (!hasCompassOrMap) {


            if (player != null) {
                client.player.sendMessage(Text.translatable("slcomp.map").styled(s -> s.withColor(Formatting.RED))
                        , true);
            }
            ci.cancel();
        }
    }
}