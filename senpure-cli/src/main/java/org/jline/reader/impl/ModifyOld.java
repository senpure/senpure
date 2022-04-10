package org.jline.reader.impl;


import net.bytebuddy.agent.ByteBuddyAgent;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class ModifyOld {
    private final static Instrumentation INSTRUMENTATION;

    static {
        INSTRUMENTATION = ByteBuddyAgent.install();
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
     * 用子类的方法覆盖父类的方法
     *
     * @param childClass 子类
     */
    public static void redefineChildMethod(Class<?> childClass) {
        try {
            ClassReader classReader = new ClassReader(childClass.getName());
            ClassNode classNode = new Rename2ParentFiledClassNode(Opcodes.ASM9, childClass);
            classReader.accept(classNode, 0);

            ClassReader superClassReader = new ClassReader(childClass.getSuperclass().getName());
            ClassWriter classWriter = new ClassWriter(0);
            ClassVisitor replaceMethodClassVisitor = new ReplaceChildMethodClassVisitor(classNode, classWriter);
            superClassReader.accept(replaceMethodClassVisitor, 0);
            redefineClass(childClass.getSuperclass(), classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class ReplaceChildMethodClassVisitor extends ClassVisitor {
        private final ClassNode classNode;

        public ReplaceChildMethodClassVisitor(ClassNode classNode, ClassVisitor classVisitor) {
            super(Opcodes.ASM9, classVisitor);
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
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.startsWith("lambda$")) {
                    methodNode.accept(cv);
                }
            }
            super.visitEnd();
        }
    }

    private static class Rename2ParentFiledClassNode extends ClassNode {

        private final String classOwnerDesc;
        private final String superClassOwnerDesc;

        public Rename2ParentFiledClassNode(int api, Class<?> clazz) {
            super(api);
            this.classOwnerDesc = clazz.getName().replace(".", "/");
            this.superClassOwnerDesc = clazz.getSuperclass().getName().replace(".", "/");
        }

        public Rename2ParentFiledClassNode(int api, String classOwnerDesc, String superClassOwnerDesc) {
            super(api);
            this.classOwnerDesc = classOwnerDesc;
            this.superClassOwnerDesc = superClassOwnerDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.startsWith("lambda$")) {
                name = name + "$redefine$0";
            }
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!name.equals("<init>")) {
                methodVisitor = new Rename2ParentFiledMethodVisitor(api, classOwnerDesc, superClassOwnerDesc, methodVisitor);
            }
            return methodVisitor;
        }

    }


    private static class Rename2ParentFiledMethodVisitor extends MethodVisitor {
        private final String classOwnerDesc;
        private final String superClassOwnerDesc;

        public Rename2ParentFiledMethodVisitor(int api, String child, String parent, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
            this.classOwnerDesc = child;
            this.superClassOwnerDesc = parent;
        }


        @Override
        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {

            descriptor = descriptor.replace(classOwnerDesc, superClassOwnerDesc);
            super.visitLocalVariable(name, descriptor, signature, start, end, index);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (owner.equals(classOwnerDesc)) {
                owner = superClassOwnerDesc;
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            if (owner.equals(classOwnerDesc)) {
                owner = superClassOwnerDesc;
            }
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }

        @Override
        public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
            for (int i = 0; i < local.length; i++) {
                if (classOwnerDesc.equals(local[i])) {

                    local[i] = superClassOwnerDesc;
                }
            }

            super.visitFrame(type, numLocal, local, numStack, stack);
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
            if (bootstrapMethodHandle.getOwner().equals(classOwnerDesc)) {
                bootstrapMethodHandle = new Handle(bootstrapMethodHandle.getTag(), superClassOwnerDesc, bootstrapMethodHandle.getName(), bootstrapMethodHandle.getDesc(), bootstrapMethodHandle.isInterface());
            }

            for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                if (bootstrapMethodArguments[i] instanceof Handle) {
                    Handle handle = (Handle) bootstrapMethodArguments[i];
                    if (handle.getOwner().equals(classOwnerDesc)) {
                        bootstrapMethodArguments[i] = new Handle(handle.getTag(), superClassOwnerDesc, handle.getName() + "$redefine$0", handle.getDesc(), handle.isInterface());
                    }
                }
            }

            super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        }
    }
}
