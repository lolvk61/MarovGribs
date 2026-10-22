package ru.marov.gribs.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;
import ru.marov.gribs.item.RussulaItem;
import ru.marov.gribs.item.SporeBombItem;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MarovGribs.MODID);

    // Eaten, mushrooms only feed you; their powers come from extracts (see ModPotions).
    // The fly agaric is the exception: it poisons and turns your stomach.
    public static final FoodProperties HOPCAP_FOOD = mushroomFood(2, 0.3F);
    public static final FoodProperties GLOWCAP_FOOD = mushroomFood(2, 0.3F);
    public static final FoodProperties PUFFBALL_FOOD = mushroomFood(4, 0.6F);
    public static final FoodProperties CHANTERELLE_FOOD = mushroomFood(3, 0.5F);
    public static final FoodProperties PARASOL_FOOD = mushroomFood(4, 0.6F);
    public static final FoodProperties RUSSULA_FOOD = mushroomFood(3, 0.4F);
    public static final FoodProperties CORDYCEPS_FOOD = mushroomFood(2, 0.3F);
    public static final FoodProperties FLY_AGARIC_FOOD = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.1F).alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300), 1.0F)
            .build();
    public static final FoodProperties MAROV_STEW_FOOD = new FoodProperties.Builder()
            .nutrition(10).saturationModifier(0.9F).usingConvertsTo(Items.BOWL)
            .build();

    public static final DeferredItem<BlockItem> HOPCAP = ITEMS.register("hopcap",
            () -> new BlockItem(ModBlocks.HOPCAP.get(), new Item.Properties().food(HOPCAP_FOOD)));
    public static final DeferredItem<BlockItem> GLOWCAP = ITEMS.register("glowcap",
            () -> new BlockItem(ModBlocks.GLOWCAP.get(), new Item.Properties().food(GLOWCAP_FOOD)));
    public static final DeferredItem<BlockItem> PUFFBALL = ITEMS.register("puffball",
            () -> new BlockItem(ModBlocks.PUFFBALL.get(), new Item.Properties().food(PUFFBALL_FOOD)));
    public static final DeferredItem<BlockItem> FLY_AGARIC = ITEMS.register("fly_agaric",
            () -> new BlockItem(ModBlocks.FLY_AGARIC.get(), new Item.Properties().food(FLY_AGARIC_FOOD)));
    public static final DeferredItem<BlockItem> CHANTERELLE = ITEMS.register("chanterelle",
            () -> new BlockItem(ModBlocks.CHANTERELLE.get(), new Item.Properties().food(CHANTERELLE_FOOD)));
    public static final DeferredItem<BlockItem> PARASOL_SPROUT = ITEMS.register("parasol_sprout",
            () -> new BlockItem(ModBlocks.PARASOL_SPROUT.get(), new Item.Properties().food(PARASOL_FOOD)));
    public static final DeferredItem<BlockItem> PARASOL_CAP = ITEMS.registerSimpleBlockItem(ModBlocks.PARASOL_CAP);
    public static final DeferredItem<BlockItem> PARASOL_STEM = ITEMS.registerSimpleBlockItem(ModBlocks.PARASOL_STEM);
    public static final DeferredItem<BlockItem> MAROV_MYCELIUM = ITEMS.registerSimpleBlockItem(ModBlocks.MAROV_MYCELIUM);

    public static final DeferredItem<RussulaItem> RUSSULA = ITEMS.register("russula",
            () -> new RussulaItem(new Item.Properties().food(RUSSULA_FOOD)));
    public static final DeferredItem<Item> MORTAR = ITEMS.register("mortar",
            () -> new Item(new Item.Properties().durability(64)));
    public static final DeferredItem<Item> CORDYCEPS = ITEMS.register("cordyceps",
            () -> new Item(new Item.Properties().food(CORDYCEPS_FOOD)));
    public static final DeferredItem<ArmorItem> DEATH_CAP_HAT = ITEMS.register("death_cap_hat",
            () -> new ArmorItem(ModArmorMaterials.DEATH_CAP, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(25))));
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
    public static final DeferredItem<DeferredSpawnEggItem> RUSSULA_SPAWN_EGG = ITEMS.register("russula_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RUSSULA, 0xD23A3A, 0xF3EFE2, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> MINER_GRIB_SPAWN_EGG = ITEMS.register("miner_grib_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MINER_GRIB, 0xE8B923, 0x9A6A45, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> CORDYCEPS_ZOMBIE_SPAWN_EGG = ITEMS.register("cordyceps_zombie_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CORDYCEPS_ZOMBIE, 0x00AFAF, 0xF08A24, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> FLYNN_FOX_SPAWN_EGG = ITEMS.register("flynn_fox_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FLYNN_FOX, 0xE07B2A, 0x4E6B3A, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> PARASOL_GRIB_SPAWN_EGG = ITEMS.register("parasol_grib_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PARASOL_GRIB, 0xE2D3B5, 0x8C5E3C, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> CHANTERELLE_GRIB_SPAWN_EGG = ITEMS.register("chanterelle_grib_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CHANTERELLE_GRIB, 0xF2A93B, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> DEATH_CAP_SPAWN_EGG = ITEMS.register("death_cap_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DEATH_CAP, 0xC2CC98, 0xEDEBDD, new Item.Properties()));

    private ModItems() {
    }

    private static FoodProperties mushroomFood(int nutrition, float saturation) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build();
    }
}
