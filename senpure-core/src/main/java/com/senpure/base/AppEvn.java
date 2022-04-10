package com.senpure.base;

import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiOutput;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 应用环境
 */
public class AppEvn {
    static {
        markPid();
    }

    private static final Logger logger = LoggerFactory.getLogger(AppEvn.class);
    private static String classRootPath;
    private static Class<?> startClass;


    private static boolean installedAnsi = false;

    public static boolean isWindowsOS() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("windows");
    }

    public static boolean isLinuxOS() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("linux");
    }

    /**
     * 获取jar的路径
     *
     * @param clazz
     * @return 如果clazz 不在jar中返回null
     */
    public static String getJarPath(Class<?> clazz) {
        URL url = clazz.getResource(clazz.getSimpleName() + ".class");
        if (url == null) {
            return null;
        }
        try {
            URI uri = url.toURI();
            String classPath = uri.getPath();
            if (classPath == null) {
                String path = getJarPath(uri);
                path = new URI(path).getPath();
                if (isWindowsOS()) {
                    int index = path.indexOf("/");
                    if (index == 0) {
                        path = path.substring(1);
                    }
                }
                return path;
            }
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getJarPath(URI uri) {
        String location = uri.toString();
        int index = location.lastIndexOf(".jar!");
        if (index == -1) {
            index = StringUtil.indexOf(location, ".", 1, true);
        }
        location = location.substring(0, index + 4);
        location = location.replace("jar:file:", "");
        return location;
    }


    /**
     * 获取class\jar的根路径如<br>
     * E:\projects\a\target\classes\com\senpure\AppEvn.class ->
     * E:\projects\a\target\classes<br>
     * E:\projects\a\target\<b><i>jar.jar</i></b>\com\senpure\AppEvn.class ->
     * E:\projects\a\target
     *
     * @return
     */
    public static String getClassRootPath(Class<?> clazz) {
        String classRootPath = null;
        try {
            URL url = clazz.getResource(clazz.getSimpleName() + ".class");
            if (url == null) {
                return getClassRootPath();
            }
            URI uri = url.toURI();

            //jar里位空
            classRootPath = uri.getPath();
            boolean cutPackage = true;
            if (classRootPath == null) {
                cutPackage = false;
                classRootPath = getJarRootPath(uri);
                uri = new URI(classRootPath);
                classRootPath = uri.getPath();
            }
            if (isWindowsOS()) {
                int index = classRootPath.indexOf("/");
                if (index == 0) {
                    classRootPath = classRootPath.substring(1);
                }
            }
            classRootPath = classRootPath.replace("/", File.separator);
            if (cutPackage) {
                if (clazz.getPackage() != null) {
                    String packpath = clazz.getPackage().getName();
                    packpath = packpath.replace(".", File.separator);
                    packpath = packpath + File.separator + clazz.getSimpleName() + ".class";
                    classRootPath = classRootPath.replace(packpath, "");
                }
            }
            while (classRootPath.charAt(classRootPath.length() - 1) == File.separatorChar) {
                classRootPath = classRootPath.substring(0, classRootPath.length() - 1);
            }

        } catch (URISyntaxException e) {

            e.printStackTrace();
        }
        return classRootPath;
    }

    /**
     * 存在多个classpath 时该值不准确,推荐使用带参数的getClassRootPath(Class clazz)<br>
     * <p>
     * 需要先调用 AppEvn.markClassRootPath()|| AppEvn.markClassRootPath(Class clazz)
     *
     * @return
     */
    public static String getClassRootPath() {

        Assert.notNull(classRootPath, "请先标记classRootPath 调用 AppEvn.markClassRootPath()|| AppEvn.markClassRootPath(Class clazz)");
//        if (classRootPath == null) {
//            markClassRootPath();
//        }
        return classRootPath;
    }


    public static void tryMarkClassRootPath() {
        if (classRootPath != null) {
            return;
        }
        Class<?> clazz = startClass;
        try {
            if (clazz == null) {
                StackTraceElement[] stacks = Thread.currentThread()
                        .getStackTrace();
                clazz = Class.forName(stacks[stacks.length - 1].getClassName());
            }
            markClassRootPath(clazz);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        }
    }

    public static void markClassRootPath() {
        Class<?> clazz = startClass;
        try {
            if (clazz == null) {
                StackTraceElement[] stacks = Thread.currentThread()
                        .getStackTrace();
                StackTraceElement stack = stacks[2];
                clazz = Class.forName(stack.getClassName());
            }
            markClassRootPath(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void markClassRootPath(Class<?> clazz) {
        String oldClassRootPath = classRootPath;
        classRootPath = getClassRootPath(clazz);
        if (oldClassRootPath != null && !classRootPath.equals(oldClassRootPath)) {
            String nowClassRootPath = classRootPath;
            classRootPath = oldClassRootPath;
            Assert.error("两个不相同的标识" + classRootPath + "," + nowClassRootPath);
        }
        if (startClass == null) {
            markStartClass(clazz);
        }
    }

    public static void markStartClass(Class<?> clazz) {
        Assert.isNull(startClass);
        startClass = clazz;
    }

    public static Class<?> getStartClass() {
        return startClass;
    }

    public static String getCallerRootPath() {
        return getClassRootPath(getCallerClass());
    }

    public static Class<?> getCallerClass() {
        StackTraceElement[] stacks = Thread.currentThread()
                .getStackTrace();
        StackTraceElement stack = stacks[3];
        Class<?> clazz = null;
        try {
            clazz = Class.forName(stack.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * @param clazz 不能是AppEvn.class
     * @return
     */
    public static boolean classInJar(Class<?> clazz) {
        return classInJar(clazz, true);
    }

    private static boolean classInJar(Class<?> clazz, boolean log) {
        URL url = clazz.getResource("");
        if (log) {
            logger.trace("clazz {}  url {}", url, clazz.getName());
        }
        if (url == null) {
            return true;
        }
        return url.toString().contains("jar:file:");
    }

    public static boolean callerInJar() {
        return classInJar(getCallerClass());
    }

    private static String getJarRootPath(URI uri) {
        String location = uri.toString();
        int index = location.indexOf(".jar!");
        if (index == -1) {
            index = StringUtil.indexOf(location, ".", 1, true);
        }
        location = location.substring(0, index);
        index = StringUtil.indexOf(location, "/", 1, true);
        location = location.substring(0, index);
        location = location.replace("jar:file:", "");
        return location;
    }

    private static String getJarPath(String location) {
        int index = location.indexOf(".jar!") + 4;
        if (index == 3) {
            index = StringUtil.indexOf(location, "!", 1, true);
        }
        location = location.substring(0, index);
        location = location.replace("jar:file:", "");
        return location;
    }

    public static String getClassPath(Class<?> clazz) {
        URL url = clazz.getResource("");
        try {
            URI uri = url.toURI();
            String location = uri.getPath();
            if (location == null) {
                location = getJarPath(uri.toString());
                location = new URI(location).getPath();
            } else {
                location += clazz.getSimpleName() + ".class";
            }
            if (isWindowsOS()) {
                int index = location.indexOf("/");
                if (index == 0) {
                    location = location.substring(1);
                }
            }
            location = location.replace("/", File.separator);

            while (location.charAt(location.length() - 1) == File.separatorChar) {
                location = location.substring(0, location.length() - 1);
            }
            return location;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCallerPath() {

        return getClassPath(getCallerClass());
    }

    public static void markPid() {
        String pid = System.getProperty("PID");
        if (pid == null) {
            pid = AppEvn.getPid();
            if (pid != null) {
                System.setProperty("PID", pid);
            }
        }

    }

    public static String getPid() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return jvmName.split("@")[0];
        } catch (Throwable ex) {
            return "0000";
        }
    }

    public static void installAnsiConsole(Class<?> clazz) {
        if (installedAnsi) {
            logger.trace("已经执行过installAnsiConsole");
            return;
        }
        try {
            Class.forName("org.fusesource.jansi.AnsiConsole");
            AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
            if (AppEvn.classInJar(clazz, false)) {
                logger.trace("执行AnsiConsole.systemInstall()");
                AnsiConsole.systemInstall();
            } else {
                logger.trace("不执行AnsiConsole.systemInstall()");
            }
        } catch (ClassNotFoundException e) {
            logger.info("不适用控制台彩色日志");
        }
        installedAnsi = true;
    }

    public static void installAnsiConsole() {

        StackTraceElement[] stacks = Thread.currentThread()
                .getStackTrace();
        StackTraceElement stack = stacks[2];
        Class<?> clazz;
        try {
            clazz = Class.forName(stack.getClassName());

            installAnsiConsole(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {


        System.out.println(getJarPath(StringUtils.class));

    }
}
