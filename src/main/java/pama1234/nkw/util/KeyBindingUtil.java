package pama1234.nkw.util;

//import committee.nova.mkb.ModernKeyBinding;
import pama1234.nkw.mixin.AccessorKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = "key.categories.nkw.all";
    public static final String DYNAMIC_CATEGORY_CONFLICTS = "key.categories.nkw.conflicts";
    public static final String DYNAMIC_CATEGORY_UNBOUND = "key.categories.nkw.unbound";
    public static final String DYNAMIC_CATEGORY_CTRL = "key.categories.nkw.ctrl";
    public static final String DYNAMIC_CATEGORY_ALT = "key.categories.nkw.alt";
    public static final String DYNAMIC_CATEGORY_SHIFT = "key.categories.nkw.shift";
    public static final String DYNAMIC_CATEGORY_NONE = "key.categories.nkw.no_modifier";

    /**
     * Get a list of all binding categories
     */
    public static ArrayList<String> getCategories() {
        return AccessorKeyBinding.getKeyCategories().stream().sorted().collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public static ArrayList<String> getCategoriesWithDynamics() {
        ArrayList<String> categories = getCategories();
        categories.add(0, DYNAMIC_CATEGORY_UNBOUND);
        categories.add(0, DYNAMIC_CATEGORY_CONFLICTS);
        categories.add(0, DYNAMIC_CATEGORY_ALL);
        categories.add(DYNAMIC_CATEGORY_CTRL);
        categories.add(DYNAMIC_CATEGORY_ALT);
        categories.add(DYNAMIC_CATEGORY_SHIFT);
        categories.add(DYNAMIC_CATEGORY_NONE);
        return categories;
    }

    @SuppressWarnings("resource")
    public static Map<Key, Integer> getBindingCountsByKey() {
        HashMap<InputUtil.Key, Integer> map = new HashMap<>();
        for (KeyBinding b : MinecraftClient.getInstance().options.allKeys) {
            map.merge(((AccessorKeyBinding) b).getBoundKey(), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }
}
