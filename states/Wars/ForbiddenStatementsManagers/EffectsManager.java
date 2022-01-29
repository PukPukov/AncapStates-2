package states.Wars.ForbiddenStatementsManagers;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class EffectsManager {
    public PotionEffect[] getEffectsToRemoveFrom(PotionEffect[] effects) {
        ArrayList<PotionEffect> effectsToRemove = new ArrayList<>();
        for (PotionEffect effect : effects) {
            if (this.isEffectForbidden(effect)) {
                effectsToRemove.add(effect);
            }
        }
        return effectsToRemove.toArray(new PotionEffect[0]);
    }

    public boolean isEffectForbidden(PotionEffect effect) {
        PotionEffectType type = effect.getType();
        if (isEffectTypeForbidden(type)) {
            return true;
        }
        if (isEffectMetaForbidden(effect)) {
            return true;
        }
        return false;
    }

    private boolean isEffectMetaForbidden(PotionEffect effect) {
        PotionEffectType type = effect.getType();
        if (type.getId() == PotionEffectType.ABSORPTION.getId()) {
            return effect.getDuration()>2400 ||
                    effect.getAmplifier()>3;
        }
        if (type.getId() == PotionEffectType.REGENERATION.getId()) {
            return effect.getDuration()>400 ||
                    effect.getAmplifier()>1;
        }
        if (type.getId() == PotionEffectType.FIRE_RESISTANCE.getId()) {
            return effect.getDuration()>6000 ||
                    effect.getAmplifier()>0;
        }
        if (type.getId() == PotionEffectType.DAMAGE_RESISTANCE.getId()) {
            return effect.getDuration()>6000 ||
                    effect.getAmplifier()>0;
        }
        return true;
    }

    public boolean isEffectTypeForbidden(PotionEffectType type) {
        return type.getId() != PotionEffectType.REGENERATION.getId() &&
                type.getId() != PotionEffectType.ABSORPTION.getId() &&
                type.getId() != PotionEffectType.FIRE_RESISTANCE.getId() &&
                type.getId() != PotionEffectType.DAMAGE_RESISTANCE.getId();
    }
}
