package com.walter.firstmod.examplemod.item;

import com.walter.firstmod.examplemod.FirstMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItem
{
    public static final DeferredRegister<Item> Items =
            DeferredRegister.create(ForgeRegistries.ITEMS, FirstMod.MODID);

    public static void register (IEventBus eventBus)
    {
        Items.register(eventBus);
    }

}
