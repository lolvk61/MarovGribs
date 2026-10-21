package ru.marov.gribs.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.item.SporeBombItem;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MarovGribs.MODID);

    public static final FoodProperties HOPCAP_FOOD = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3F).alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 600, 1), 1.0F)
            .build();
    public static final FoodProperties GLOWCAP_FOOD = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3F).alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 1200), 1.0F)
            .build();
    public static final FoodProperties PUFFBALL_FOOD = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.6F)
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 300), 1.0F)
            .build();
    public static final FoodProperties FLY_AGARIC_FOOD = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.1F).alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 1.0F)
            .build();
    public static final FoodProperties MAROV_STEW_FOOD = new FoodProperties.Builder()
            .nutrition(8).saturationModifier(0.8F).alwaysEdible().usingConvertsTo(Items.BOWL)
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 1200, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 2400), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 1200), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200), 1.0F)
            .build();

    public static final DeferredItem<BlockItem> HOPCAP = ITEMS.register("hopcap",
            () -> new BlockItem(ModBlocks.HOPCAP.get(), new Item.Properties().food(HOPCAP_FOOD)));
    public static final DeferredItem<BlockItem> GLOWCAP = ITEMS.register("glowcap",
            () -> new BlockItem(ModBlocks.GLOWCAP.get(), new Item.Properties().food(GLOWCAP_FOOD)));
    public static final DeferredItem<BlockItem> PUFFBALL = ITEMS.register("puffball",
            () -> new BlockItem(ModBlocks.PUFFBALL.get(), new Item.Properties().food(PUFFBALL_FOOD)));
    public static final DeferredItem<BlockItem> FLY_AGARIC = ITEMS.register("fly_agaric",
            () -> new BlockItem(ModBlocks.FLY_AGARIC.get(), new Item.Properties().food(FLY_AGARIC_FOOD)));

    public static final DeferredItem<SporeBombItem> SPORE_BOMB = ITEMS.register("spore_bomb",
            () -> new SporeBombItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> MAROV_STEW = ITEMS.register("marov_stew",
            () -> new Item(new Item.Properties().stacksTo(1).food(MAROV_STEW_FOOD)));

    public static final DeferredItem<DeferredSpawnEggItem> HOPLING_SPAWN_EGG = ITEMS.register("hopling_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HOPLING, 0x6DBE34, 0xE9E3C6, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> PUFFER_SPAWN_EGG = ITEMS.register("puffer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PUFFER, 0xEDE6CF, 0xA68F62, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> AGARIC_GUARD_SPAWN_EGG = ITEMS.register("agaric_guard_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.AGARIC_GUARD, 0xD7322B, 0xFFFFFF, new Item.Properties()));

    private ModItems() {
    }
}
