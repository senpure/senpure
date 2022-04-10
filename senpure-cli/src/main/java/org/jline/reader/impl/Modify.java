package org.jline.reader.impl;


import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.commons.RemappingClassAdapter;
import jdk.internal.org.objectweb.asm.commons.SimpleRemapper;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import net.bytebuddy.agent.ByteBuddyAgent;
import sun.misc.Unsafe;


import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Modify {
    public final static Instrumentation INSTRUMENTATION;

    public final static int ASM_API = Opcodes.ASM5;
    private static final jdk.internal.misc.Unsafe UNSAFE;

    static {
        INSTRUMENTATION = ByteBuddyAgent.install();
        UNSAFE = getUnSafe();
    }

    /**
     * 重新加载类
     *
     * @param theClass
     * @param theClassFile
     */
    public static void redefineClass(Class<?> theClass, byte[] theClassFile) {
        try {
            INSTRUMENTATION.redefineClasses(new ClassDefinition(theClass, theClassFile));
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接定义一个类
     *
     * @param className
     * @param bytes
     * @return
     */
    public static Class<?> defineClass(String className, byte[] bytes) {
        if (UNSAFE != null) {

            return UNSAFE.defineClass(className, bytes, 0, bytes.length, null, null);
        }
        return null;
    }

    /**
     * 使用子类的方法替换父类的方法
     * <br>
     * <b>暂时不支持在子类方法的lambda表达式中调用父类的方法或字段，如果是字段可采用本地变量的方式使用！</b>
     *
     * @param childClass
     */
    public static void redefineSuperMethod(Class<?> childClass) {
        try {
            ClassReader classReader = new ClassReader(childClass.getName());
            ClassNode originalChildClassNode = new ClassNode();
            String child = childClass.getName().replace(".", "/");
            String parent = childClass.getSuperclass().getName().replace(".", "/");
            Map<String, String> mapping = new HashMap<>();
            mapping.put(child, parent);
            classReader.accept(originalChildClassNode, ClassReader.EXPAND_FRAMES);
            for (MethodNode method : originalChildClassNode.methods) {
                // System.out.println(method.name +" "+method.access +" "+Modifier.toString(method.access));
                if (method.name.startsWith("lambda$")) {
                    if (Modifier.isStatic(method.access)) {
                        mapping.put(child + "." + method.name + method.desc, method.name + "$rename$0");
                    } else {
                        throw new RuntimeException("暂时不支持在子类方法的lambda表达式中调用父类的方法或字段，如果是字段可采用本地变量的方式使用！");
                    }

                    //mapping.put(child + "." + method.name + method.desc, method.name.replace("lambda$","l$$e"));
                }
            }

            for (InnerClassNode innerClass : originalChildClassNode.innerClasses) {
                if (innerClass.name.startsWith("java/lang/invoke/MethodHandles")) {
                    continue;
                }
                if (innerClass.outerName != null && !innerClass.outerName.equals(child)) {
                    continue;
                }
                //System.out.println(innerClass.name);
                String original = innerClass.name.replace(".", "/");
                String target = original + "$Rename$0";
                Map<String, String> innerMapping = new HashMap<>(mapping);
                innerMapping.put(innerClass.name.replace(".", "/"), target);
                mapping.put(innerClass.name.replace(".", "/"), target);
                ClassReader innerClassReader = new ClassReader(innerClass.name);
                ClassNode originalChildInnerClassNode = new ClassNode();
                innerClassReader.accept(originalChildInnerClassNode, ClassReader.EXPAND_FRAMES);
                //TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out));
                // originalChildInnerClassNode.accept(traceClassVisitor);
                //  ClassWriter originalInnerClassWriter = new ClassWriter(0);

                ClassNode renamedChildInnerClassNode = new ClassNode();
                ClassVisitor classVisitor = new ModifyInnerClassAccessClassVisitor(renamedChildInnerClassNode);
                RemappingClassAdapter classRemapper = new RemappingClassAdapter(classVisitor, new SimpleRemapper(innerMapping));
                originalChildInnerClassNode.accept(classRemapper);
                // TraceClassVisitor renamedTraceClassVisitor = new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out));
                // renamedChildInnerClassNode.accept(renamedTraceClassVisitor);
                ClassWriter classWriter = new ClassWriter(0);
                renamedChildInnerClassNode.accept(classWriter);
                defineClass(target.replace("/", "."), classWriter.toByteArray());
            }

            ClassNode renamedChildClassNode = new ClassNode();
            // ClassRemapper classRemapper = new ClassRemapper(renamedChildClassNode, new SimpleRemapper(mapping));
            RemappingClassAdapter classRemapper = new RemappingClassAdapter(renamedChildClassNode, new SimpleRemapper(mapping));
            originalChildClassNode.accept(classRemapper);
            // TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out));
            // originalChildClassNode.accept(traceClassVisitor);

            replaceChildMethod(childClass.getSuperclass(), renamedChildClassNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void replaceChildMethod(Class<?> superClass, ClassNode renamedChildClassNode) {
        try {
            ClassNode originalSuperClassNode = new ClassNode();
            ClassReader classReader = new ClassReader(superClass.getName());
            classReader.accept(originalSuperClassNode, ClassReader.EXPAND_FRAMES);

            ClassWriter originalSuperClassWriter = new ClassWriter(0);
            originalSuperClassNode.accept(originalSuperClassWriter);

            //  Asm.dumpClass(superClass.getSimpleName() + "$Original", originalSuperClassWriter.toByteArray());

            ClassWriter classWriter = new ClassWriter(0);
            ClassVisitor classVisitor = new ReplaceChildMethodClassVisitor(renamedChildClassNode, classWriter);

            //
            originalSuperClassNode.accept(classVisitor);
            // Asm.trace(classWriter.toByteArray());
            //  Asm.dumpClass(superClass.getSimpleName() + "$Redefine", classWriter.toByteArray());
            // redefineClass(superClass, ParentDump.dump());
            redefineClass(superClass, classWriter.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static jdk.internal.misc.Unsafe getUnSafe() {
        Field theUnsafe;
        try {
            theUnsafe = jdk.internal.misc.Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (jdk.internal.misc.Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class ReplaceChildMethodClassVisitor extends ClassVisitor {
        private final ClassNode classNode;

        public ReplaceChildMethodClassVisitor(ClassNode classNode, ClassVisitor classVisitor) {
            super(ASM_API, classVisitor);
            this.classNode = classNode;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (!name.equals("<init>")) {
                MethodNode updateMethodNode = classNode.methods.stream().filter(methodNode -> methodNode.access == access && methodNode.name.equals(name) && methodNode.desc.equals(descriptor)).findFirst().orElse(null);
                if (updateMethodNode != null) {
                    updateMethodNode.accept(cv);
                    return null;
                }
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            for (MethodNode method : classNode.methods) {
                if (method.name.startsWith("lambda$")) {
                    method.accept(cv);
                }

            }
            super.visitEnd();
        }
    }

    private static class ModifyInnerClassAccessClassVisitor extends ClassVisitor {


        public ModifyInnerClassAccessClassVisitor(ClassVisitor classVisitor) {
            super(ASM_API, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            access = Opcodes.ACC_PUBLIC;
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            access = Opcodes.ACC_PUBLIC;
            super.visit(version, access, name, signature, superName, interfaces);
        }
    }

}
