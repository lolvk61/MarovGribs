package ru.marov.gribs.registry;

import java.util.EnumMap;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, MarovGribs.MODID);

    /** The death cap's own cap, worn as a helmet: weak as armour, but poison and spores can't get through. */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DEATH_CAP = MATERIALS.register("death_cap", () -> {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.HELMET, 2);
        defense.put(ArmorItem.Type.CHESTPLATE, 0);
        defense.put(ArmorItem.Type.LEGGINGS, 0);
        defense.put(ArmorItem.Type.BOOTS, 0);
        defense.put(ArmorItem.Type.BODY, 0);
        return new ArmorMaterial(defense, 15, SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(ModItems.FLY_AGARIC.get()),
                List.of(new ArmorMaterial.Layer(MarovGribs.id("death_cap"))), 0.0F, 0.0F);
    });

    private ModArmorMaterials() {
    }
}
