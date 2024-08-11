package moe.ingstar.ee.data;

import com.mojang.serialization.Codec;
import moe.ingstar.ee.EnchantmentExpansion;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModModelProvider extends FabricCodecDataProvider {


    protected ModModelProvider(FabricDataOutput dataOutput, CompletableFuture registriesFuture, DataOutput.OutputType outputType, String directoryName, Codec codec) {
        super(dataOutput, registriesFuture, outputType, directoryName, codec);
    }

    @Override
    protected void configure(BiConsumer provider, RegistryWrapper.WrapperLookup lookup) {

    }

    @Override
    public String getName() {
        return "";
    }
}
