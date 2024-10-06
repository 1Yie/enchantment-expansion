package moe.ingstar.ee.util.tool;

import moe.ingstar.ee.EnchantmentExpansion;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;

public record CooldownPayload(int cooldown) implements CustomPayload {
    // 定义标识符
    public static final Id<CooldownPayload> ID = CustomPayload.id(EnchantmentExpansion.MOD_ID + ":" + "cooldown_packet");

    @Override
    public Id<CooldownPayload> getId() {
        return ID;
    }

    // 将数据写入数据包
    public void write(PacketByteBuf buf) {
        buf.writeInt(cooldown);
    }


    public static CooldownPayload read(PacketByteBuf buf) {
        return new CooldownPayload(buf.readInt());
    }
}
