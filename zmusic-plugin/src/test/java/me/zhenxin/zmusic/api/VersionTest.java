package me.zhenxin.zmusic.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

    @Test
    void treatsCalverServerAsNewerThanLegacyMinecraftVersions() {
        Version version = version("26.1.2-R0.1-SNAPSHOT");

        assertEquals("26.1.2", version.getVersion());
        assertFalse(version.isLowerThan("1.8"));
        assertFalse(version.isLowerThan("1.9"));
        assertFalse(version.isLowerThan("1.12"));
        assertTrue(version.isHigherThan("1.12"));
    }

    @Test
    void keepsLegacyVersionComparisonBehavior() {
        Version version = version("1.7.10-R0.1-SNAPSHOT");

        assertTrue(version.isLowerThan("1.8"));
        assertFalse(version.isHigherThan("1.8"));
        assertTrue(version.isEquals("1.7.10"));
    }

    @Test
    void comparesMissingSegmentsAsZero() {
        Version version = version("1.9-R0.1-SNAPSHOT");

        assertFalse(version.isLowerThan("1.9.0"));
        assertFalse(version.isHigherThan("1.9.0"));
    }

    private static Version version(String rawVersion) {
        return new Version() {
            @Override
            public String getRawVersion() {
                return rawVersion;
            }
        };
    }
}
