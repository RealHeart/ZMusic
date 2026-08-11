package me.zhenxin.zmusic.addon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PApiHookTest {

    @Test
    void remainsRegisteredWhenPlaceholderApiReloads() {
        assertTrue(new PApiHook().persist());
    }
}
