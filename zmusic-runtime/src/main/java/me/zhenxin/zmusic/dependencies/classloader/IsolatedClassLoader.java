package me.zhenxin.zmusic.dependencies.classloader;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"AlibabaClassMustHaveAuthor", "AlibabaConstantFieldShouldBeUpperCase", "MismatchedQueryAndUpdateOfCollection"})
public class IsolatedClassLoader extends URLClassLoader {

    private static final Set<String> excludeClasses = new HashSet<>();
    private static final boolean isEnabled = false;

    public IsolatedClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> loadedClass = findLoadedClass(name);

            if (loadedClass == null) {
                // check isolated classes
                if (!excludeClasses.contains(name)) {
                    try {
                        loadedClass = findClass(name);
                    } catch (ClassNotFoundException ignored) {
                    }
                }
                // check the parent class loader
                if (loadedClass == null) {
                    ClassLoader parent = getParent();
                    if (parent != null) {
                        loadedClass = parent.loadClass(name);
                    }
                }
            }

            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }
}