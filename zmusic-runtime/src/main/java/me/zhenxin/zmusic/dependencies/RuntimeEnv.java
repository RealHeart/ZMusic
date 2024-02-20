package me.zhenxin.zmusic.dependencies;

import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependencies;
import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;
import me.zhenxin.zmusic.dependencies.utils.IO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TabooLib
 * taboolib.common.env.RuntimeEnv
 *
 * @author sky
 * @since 2021/6/15 6:23 下午
 */
@SuppressWarnings({"CallToPrintStackTrace", "ResultOfMethodCallIgnored", "unused"})
public class RuntimeEnv {

    public static final RuntimeEnv ENV = new RuntimeEnv();

    private static String defaultLibrary = "libraries";
    private static final String defaultRepositoryCentral = "https://maven.aliyun.com/repository/public";

    public void setup(String dataFolder) {
        defaultLibrary = dataFolder + "/" + defaultLibrary;
        try {
            loadDependency(KotlinEnv.class, true);
        } catch (NoClassDefFoundError ignored) {
        }
    }

    public void inject(@NotNull Class<?> clazz) {
        loadDependency(clazz, false);
    }

    private boolean test(String path) {
        String test = path.startsWith("!") ? path.substring(1) : path;
        return !test.isEmpty() && ClassAppender.isExists(test);
    }

    public void loadDependency(@NotNull Class<?> clazz, boolean initiative) {
        File baseFile = new File(defaultLibrary);
        RuntimeDependency[] dependencies = null;
        if (clazz.isAnnotationPresent(RuntimeDependency.class)) {
            dependencies = clazz.getAnnotationsByType(RuntimeDependency.class);
        } else {
            RuntimeDependencies annotation = clazz.getAnnotation(RuntimeDependencies.class);
            if (annotation != null) {
                dependencies = annotation.value();
            }
        }
        if (dependencies != null) {
            for (RuntimeDependency dependency : dependencies) {
                if (dependency.initiative() && !initiative) {
                    continue;
                }
                String allTest = dependency.test();
                List<String> tests = new ArrayList<>();
                if (allTest.contains(",")) {
                    tests.addAll(Arrays.asList(allTest.split(",")));
                } else {
                    tests.add(allTest);
                }
                if (tests.stream().allMatch(this::test)) {
                    continue;
                }
                List<JarRelocation> relocation = new ArrayList<>();
                String[] relocate = dependency.relocate();
                if (relocate.length % 2 != 0) {
                    throw new IllegalArgumentException("unformatted relocate");
                }
                for (int i = 0; i + 1 < relocate.length; i += 2) {
                    String pattern = relocate[i].startsWith("!") ? relocate[i].substring(1) : relocate[i];
                    String relocatePattern = relocate[i + 1].startsWith("!") ? relocate[i + 1].substring(1) : relocate[i + 1];
                    relocation.add(new JarRelocation(pattern, relocatePattern));
                }
                try {
                    String url = dependency.value().startsWith("!") ? dependency.value().substring(1) : dependency.value();
                    loadDependency(url, baseFile, relocation, dependency.repository(), dependency.ignoreOptional(), dependency.ignoreException(), dependency.transitive(), dependency.isolated(), dependency.initiative(), dependency.scopes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void loadDependency(@NotNull String url) throws IOException {
        loadDependency(url, new File(defaultLibrary));
    }

    public void loadDependency(@NotNull String url, @Nullable String repository) throws IOException {
        loadDependency(url, new File(defaultLibrary), repository);
    }

    public void loadDependency(@NotNull String url, @NotNull List<JarRelocation> relocation) throws IOException {
        loadDependency(url, new File(defaultLibrary), relocation, null, true, false, true, false, false, new DependencyScope[]{DependencyScope.RUNTIME, DependencyScope.COMPILE});
    }

    public void loadDependency(@NotNull String url, @NotNull File baseDir) throws IOException {
        loadDependency(url, baseDir, null);
    }

    public void loadDependency(@NotNull String url, @NotNull File baseDir, @Nullable String repository) throws IOException {
        loadDependency(url, baseDir, new ArrayList<>(), repository, true, false, true, false, false, new DependencyScope[]{DependencyScope.RUNTIME, DependencyScope.COMPILE});
    }

    public void loadDependency(@NotNull String url, @NotNull File baseDir, @NotNull List<JarRelocation> relocation, @Nullable String repository, boolean ignoreOptional, boolean ignoreException, boolean transitive, boolean isolated, boolean initiative, @NotNull DependencyScope[] dependencyScopes) throws IOException {
        String[] args = url.split(":");
        DependencyDownloader downloader = new DependencyDownloader(baseDir, relocation);
        repository = defaultRepositoryCentral;
        downloader.addRepository(new Repository(repository));
        downloader.setIgnoreOptional(ignoreOptional);
        downloader.setIgnoreException(ignoreException);
        downloader.setDependencyScopes(dependencyScopes);
        downloader.setTransitive(transitive);
        downloader.setIsolated(isolated);
        downloader.setInitiative(initiative);
        // 解析依赖
        File pomFile = new File(baseDir, String.format("%s/%s/%s/%s-%s.pom", args[0].replace('.', '/'), args[1], args[2], args[1], args[2]));
        File pomFile1 = new File(pomFile.getPath() + ".sha1");
        // 验证文件完整性
        if (IO.validation(pomFile, pomFile1)) {
            downloader.loadDependencyFromInputStream(pomFile.toPath().toUri().toURL().openStream());
        } else {
            String pom = String.format("%s/%s/%s/%s/%s-%s.pom", repository, args[0].replace('.', '/'), args[1], args[2], args[1], args[2]);
            try {
                downloader.loadDependencyFromInputStream(new URL(pom).openStream());
            } catch (FileNotFoundException ex) {
                if (ex.toString().contains("#KOTLIN_VERSION#")) {
                    return;
                }
                throw ex;
            }
        }
        // 加载自身
        Dependency current = new Dependency(args[0], args[1], args[2], DependencyScope.RUNTIME);
        if (transitive) {
            downloader.injectClasspath(downloader.loadDependency(downloader.getRepositories(), current));
        } else {
            downloader.injectClasspath(Collections.singleton(current));
        }
    }
}