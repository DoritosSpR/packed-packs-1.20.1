package io.github.fishstiz.packed_packs.config;

import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.util.PackUtil;
import io.github.fishstiz.packed_packs.util.ResourceUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.FileUtil;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

// Nota: He quitado la referencia a fidgetz.util.lang.ObjectsUtil.mapOrDefault para evitar dependencias externas rotas
// Puedes usar Objects.requireNonNull o un helper local.

public class Profile implements PackOptions, Serializable {
    public static final int NAME_MAX_LENGTH = 32;
    static final String PROFILE_EXTENSION = ".profile.json";
    private static final String PROFILE_EXTENSION_QUOTE = Pattern.quote(PROFILE_EXTENSION);
    private boolean locked = false;
    private String name;
    private Map<String, PackOverride> overrides = new Object2ObjectOpenHashMap<>();
    private Set<String> packIds = new ObjectLinkedOpenHashSet<>();
    transient String id;
    transient Path saveFolder;
    transient boolean temp = false;

    Profile() {
        this.id = createTempId();
    }

    Profile(String name, Path saveFolder) {
        this.temp = true;
        this.saveFolder = saveFolder;
        this.name = trimName(name);
        this.id = findAvailableId(this.saveFolder, this.name);
    }

    private Profile(String name, Set<String> packIds, Map<String, PackOverride> overrides, Path saveFolder) {
        this(name, saveFolder);
        this.packIds = new ObjectLinkedOpenHashSet<>(packIds);
        this.overrides = new Object2ObjectOpenHashMap<>(overrides);
        this.overrides.replaceAll((id, override) -> new PackOverride(override.hidden(), override.required(), override.position()));
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    void setName(String name) {
        if (!this.isLocked()) {
            this.name = trimName(name);
            if (this.temp && this.saveFolder != null) {
                this.id = findAvailableId(this.saveFolder, this.name);
            }
        }
    }

    public Profile copy() {
        String profileName = this.name;
        if (profileName != null && !profileName.isBlank()) {
            profileName += " - " + ResourceUtil.getText("profile.copy").getString();
        }
        return new Profile(profileName, this.packIds, this.overrides, this.saveFolder);
    }

    public boolean includes(Pack pack) {
        return this.packIds.contains(pack.getId());
    }

    public List<String> getPackIds() {
        return new ArrayList<>(this.packIds);
    }

    public void setPacks(Collection<Pack> selected) {
        if (!this.locked) {
            selected = PackUtil.flattenPacks(selected);
            this.packIds = new ObjectLinkedOpenHashSet<>(PackUtil.extractPackIds(selected));
        }
    }

    public void syncPacks(Collection<Pack> available, Collection<Pack> selected) {
        if (!this.locked) {
            available = PackUtil.flattenPacks(available);
            selected = PackUtil.flattenPacks(selected);
            this.packIds = new ObjectLinkedOpenHashSet<>(PackUtil.extractPackIds(selected));
            Set<String> availableIds = new ObjectOpenHashSet<>(PackUtil.extractPackIds(available));
            this.overrides.entrySet().removeIf(entry -> {
                PackOverride override = entry.getValue();
                String packId = entry.getKey();
                return !override.hasOverride() || (!this.packIds.contains(packId) && !availableIds.contains(packId));
            });
        }
    }

    public void setHidden(boolean hidden, Pack pack) {
        this.applyOrRemoveOverride(pack.getId(), hidden ? true : null, PackOverride::setHidden);
    }

    public void setRequired(@Nullable Boolean required, Pack pack) {
        if (!Boolean.FALSE.equals(required) || !PackUtil.isEssential(pack)) {
            this.applyOrRemoveOverride(pack.getId(), required, PackOverride::setRequired);
        }
    }

    public void setPosition(@Nullable PackOverride.Position position, Pack pack) {
        this.applyOrRemoveOverride(pack.getId(), position, PackOverride::setPosition);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public boolean isHidden(Pack pack) {
        PackOverride o = this.overrides.get(pack.getId());
        return o != null && Boolean.TRUE.equals(o.hidden());
    }

    @Override
    public boolean isRequired(Pack pack) {
        PackOverride o = this.overrides.get(pack.getId());
        return o != null && Boolean.TRUE.equals(o.required());
    }

    @Override
    public boolean isFixed(Pack pack) {
        if (this.overridesPosition(pack)) {
            PackOverride.Position pos = this.overrides.get(pack.getId()).position();
            return pos != null && pos.fixed();
        }
        return false;
    }

    @Override
    public @Nullable Pack.Position getPosition(Pack pack) {
        if (this.overridesPosition(pack)) {
            PackOverride.Position pos = this.overrides.get(pack.getId()).position();
            return pos != null ? pos.get(pack) : null;
        }
        return null;
    }

    public boolean overridesPosition(Pack pack) {
        PackOverride entry = this.overrides.get(pack.getId());
        return entry != null && entry.position() != null;
    }

    private <T> void applyOrRemoveOverride(String packId, T property, BiConsumer<PackOverride, T> setter) {
        PackOverride override = this.overrides.computeIfAbsent(packId, id -> new PackOverride());
        setter.accept(override, property);
        if (!override.hasOverride()) this.overrides.remove(packId);
    }

    private static String trimName(String name) {
        if (name == null) return null;
        return name.length() <= NAME_MAX_LENGTH ? name : name.substring(0, NAME_MAX_LENGTH);
    }

    private static String createTempId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SSS"));
    }

    private static String findAvailableId(Path saveFolder, String name) {
        try {
            // En 1.20.1 FileUtil.findAvailableName devuelve la ruta completa o el nombre con incremento
            return removeExtension(FileUtil.findAvailableName(saveFolder, name, PROFILE_EXTENSION));
        } catch (IOException e) {
            return createTempId();
        }
    }

    static String removeExtension(String id) {
        return id.replaceFirst(PROFILE_EXTENSION_QUOTE + "$", "");
    }
}
