package pama1234.nkw.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(KeyBinding.class)
public interface AccessorKeyBinding {
    @Accessor
    InputUtil.Key getBoundKey();

    @Accessor("KEY_CATEGORIES")
    static Set<String> getKeyCategories() {
        throw new AssertionError();
    }
}