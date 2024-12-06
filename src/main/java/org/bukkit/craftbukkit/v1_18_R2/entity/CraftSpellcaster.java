package org.bukkit.craftbukkit.v1_18_R2.entity;

import catserver.server.CatServer;
import catserver.server.utils.EnumHelper;
import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Spellcaster;

import java.util.ArrayList;
import java.util.List;

public class CraftSpellcaster extends CraftIllager implements Spellcaster {

    public CraftSpellcaster(CraftServer server, SpellcasterIllager entity) {
        super(server, entity);
    }

    @Override
    public SpellcasterIllager getHandle() {
        return (SpellcasterIllager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSpellcaster";
    }

    @Override
    public Spell getSpell() {
        return toBukkitSpell(getHandle().getCurrentSpell());
    }

    @Override
    public void setSpell(Spell spell) {
        Preconditions.checkArgument(spell != null, "Use Spell.NONE");

        getHandle().setIsCastingSpell(toNMSSpell(spell));
    }

    // CatServer start
    public static Spell toBukkitSpell(SpellcasterIllager.IllagerSpell spell) {
        try {
            return Spellcaster.Spell.valueOf(spell.name());
        } catch (IllegalArgumentException e) {
            var newTypes = new ArrayList<Spell>();
            var forgeCount = SpellcasterIllager.IllagerSpell.values().length;
            for (var id = Spellcaster.Spell.values().length; id < forgeCount; id++) {
                var name = SpellcasterIllager.IllagerSpell.values()[id].name();
                var newPhase = EnumHelper.makeEnum(Spellcaster.Spell.class, name, id, List.of(), List.of());
                newTypes.add(newPhase);
                CatServer.LOGGER.debug("Save-IllagerSpell: {} - {}", name, newPhase);
            }
            EnumHelper.addEnums(Spellcaster.Spell.class, newTypes);
            return toBukkitSpell(spell);
        }
    }
    // CatServer end

    public static SpellcasterIllager.IllagerSpell toNMSSpell(Spell spell) {
        return SpellcasterIllager.IllagerSpell.byId(spell.ordinal());
    }
}
