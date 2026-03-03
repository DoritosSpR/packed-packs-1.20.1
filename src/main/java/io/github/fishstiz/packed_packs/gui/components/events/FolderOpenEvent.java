package io.github.fishstiz.packed_packs.gui.components.events;

import io.github.fishstiz.packed_packs.gui.components.pack.PackList;
import io.github.fishstiz.packed_packs.pack.FolderPack;

public record FolderOpenEvent(PackList target, FolderPack opened) implements FileEvent {
}
