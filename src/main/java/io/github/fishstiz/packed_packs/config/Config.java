package io.github.fishstiz.packed_packs.config;

import io.github.fishstiz.fidgetz.util.lang.CollectionsUtil;
import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.gui.components.pack.Query;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;

public class Config implements Serializable {
    private static final String FILENAME = "config.json";
    private static final Config INSTANCE = loadOrCreate();
    private boolean devMode = false;
    private boolean showActionBar = false;
    private boolean hideIncompatible = false;
    private String sort;
    private final ResourcePacks resourcepacks = new ResourcePacks();
    private final DataPacks datapacks = new DataPacks();

    private Config() {}

    private static Path getPath() {
        return PackedPacks.getConfigDir().resolve(FILENAME);
    }

    private static Config loadOrCreate() {
        return JsonLoader.loadOrCreateJson(getPath(), Config.class, Config::new);
    }

    public static Config get() {
        return INSTANCE;
    }

    public Packs get(PackType packType) {
        // Switch de Java 17
        switch (packType) {
            case CLIENT_RESOURCES: return this.getResourcepacks();
            case SERVER_DATA: return this.getDatapacks();
            default: throw new IllegalArgumentException("Unknown pack type");
        }
    }

    public void save() {
        JsonLoader.saveJson(this, getPath());
    }

    public ResourcePacks getResourcepacks() { return this.resourcepacks; }
    public DataPacks getDatapacks() { return this.datapacks; }

    public abstract static class Packs implements Serializable {
        private boolean replaceOriginal = true;
        private boolean hideIncompatibleWarnings = false;
        private final List<String> additionalFolders = new ObjectArrayList<>();
        private boolean rememberLastViewedProfile = false;
        private @Nullable String lastViewedProfile = null;
        private List<String> profileOrder = new ObjectArrayList<>();
        private transient @Nullable List<Profile> availableProfiles;
        private transient @Nullable Profile cachedLastViewedProfile = null;

        public abstract PackType packType();

        public List<Profile> getProfiles() {
            if (this.availableProfiles == null) {
                List<Profile> profiles = Profiles.getAll(this.packType(), Util.backgroundExecutor());
                
                Map<String, Integer> orderMap = new Object2IntOpenHashMap<>(this.profileOrder.size());
                for (int i = 0; i < this.profileOrder.size(); i++) {
                    orderMap.put(this.profileOrder.get(i), i);
                }

                profiles.sort(Comparator.<Profile>comparingInt(p -> 
                    orderMap.containsKey(p.getId()) ? orderMap.get(p.getId()) : Integer.MAX_VALUE
                ).thenComparing(Profile::getName));

                this.availableProfiles = profiles;
            }
            return Collections.unmodifiableList(this.availableProfiles);
        }

        // CAMBIO: Usamos Collection en lugar de SequencedCollection (Java 21)
        public void setProfileOrder(Collection<Profile> profiles) {
            this.profileOrder = new ArrayList<>();
            for (Profile p : profiles) {
                this.profileOrder.add(p.getId());
            }
        }

        public void addProfile(Profile profile) {
            this.profileOrder.add(profile.getId());
            if (this.availableProfiles != null) {
                this.availableProfiles.add(profile);
            }
        }
        
        // ... (resto de métodos getters/setters estándar)
    }

    public static final class DataPacks extends Packs {
        @Override
        public PackType packType() { return PackType.SERVER_DATA; }
    }

    public static final class ResourcePacks extends Packs {
        private boolean applyOnClose = true;
        @Override
        public PackType packType() { return PackType.CLIENT_RESOURCES; }
        public boolean isApplyOnClose() { return this.applyOnClose; }
        public void setApplyOnClose(boolean applyOnClose) { this.applyOnClose = applyOnClose; }
    }
}
