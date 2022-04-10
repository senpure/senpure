package com.senpure.io.server;

import java.lang.reflect.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TypeParameterMatcher {
    /**
     * 寻找泛型的具体类型 {@code object} 必须明确的唯一的泛型实现
     * <ul>  java.util.hashMap 的实例不能获取 </ul>
     * <ul> xx.XXMap  extends java.util.hashMap &lt;String,Integer>  的实例可以获取 </ul>
     * @param object                 待寻找的对象
     * @param parametrizedSuperclass 泛型的接口或父类
     * @param typeParamName          泛型参数
     * @return 泛型的实际类型
     */
    public static Class<?> find(final Object object, Class<?> parametrizedSuperclass, String typeParamName) {


        if (parametrizedSuperclass.isInterface()) {
            return findInterface(object, parametrizedSuperclass, typeParamName);
        } else {

            return findClass(object, parametrizedSuperclass, typeParamName);
        }

    }

    private static Class<?> findInterface(final Object object, Class<?> parametrizedSuperclass, String typeParamName) {
        final Class<?> thisClass = object.getClass();
        Class<?> currentClass = thisClass;
        for (; ; ) {
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> face : interfaces) {

                if (face == parametrizedSuperclass || parametrizedSuperclass.isAssignableFrom(face)) {
                    TypeVariable<?>[] typeParams = face.getTypeParameters();
                    int typeParamIndex = getTypeParamIndex(parametrizedSuperclass, typeParamName, typeParams);
                    Type actualType = currentClass.getGenericInterfaces()[typeParamIndex];
                    if (actualType instanceof ParameterizedType) {
                        Type[] actualTypeParams = ((ParameterizedType) actualType).getActualTypeArguments();
                        Type actualTypeParam = actualTypeParams[typeParamIndex];

                        Class<?> clazz = actualTypeParamClass(actualTypeParam);
                        if (clazz != null) {
                            return clazz;
                        }
                        if (actualTypeParam instanceof TypeVariable) {
                            // Resolved type parameter points to another type parameter.
                            TypeVariable<?> v = (TypeVariable<?>) actualTypeParam;

                            if (!(v.getGenericDeclaration() instanceof Class)) {
                                return Object.class;
                            }

                            parametrizedSuperclass = (Class<?>) v.getGenericDeclaration();
                            typeParamName = v.getName();
                            if (parametrizedSuperclass.isAssignableFrom(thisClass)) {

                                return findClass(object, currentClass, typeParamName);
                            } else {
                                return Object.class;
                            }
                        }

                        return fail(thisClass, typeParamName);
                    } else {
                        return Object.class;
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                return fail(thisClass, typeParamName);
            }
        }

    }

    private static Class<?> actualTypeParamClass(Type actualTypeParam) {
        if (actualTypeParam instanceof ParameterizedType) {
            actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
        }
        if (actualTypeParam instanceof Class) {
            return (Class<?>) actualTypeParam;
        }
        if (actualTypeParam instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
            if (componentType instanceof ParameterizedType) {
                componentType = ((ParameterizedType) componentType).getRawType();
            }
            if (componentType instanceof Class) {
                return Array.newInstance((Class<?>) componentType, 0).getClass();
            }
        }
        return null;
    }

    private static Class<?> findClass(
            final Object object, Class<?> parametrizedSuperclass, String typeParamName) {

        final Class<?> thisClass = object.getClass();
        Class<?> currentClass = thisClass;
        if (currentClass == parametrizedSuperclass) {
            return fail(thisClass, typeParamName);
        }
        for (; ; ) {
            if (currentClass.getSuperclass() == parametrizedSuperclass) {

                TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
                int typeParamIndex = getTypeParamIndex(parametrizedSuperclass, typeParamName, typeParams);

                Type genericSuperType = currentClass.getGenericSuperclass();
                if (!(genericSuperType instanceof ParameterizedType)) {
                    return Object.class;
                }

                Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();

                Type actualTypeParam = actualTypeParams[typeParamIndex];

                Class<?> clazz = actualTypeParamClass(actualTypeParam);
                if (clazz != null) {
                    return clazz;
                }
                if (actualTypeParam instanceof TypeVariable) {
                    // Resolved type parameter points to another type parameter.
                    TypeVariable<?> v = (TypeVariable<?>) actualTypeParam;
                    currentClass = thisClass;
                    if (!(v.getGenericDeclaration() instanceof Class)) {
                        return Object.class;
                    }

                    parametrizedSuperclass = (Class<?>) v.getGenericDeclaration();
                    typeParamName = v.getName();
                    if (parametrizedSuperclass.isAssignableFrom(thisClass)) {
                        continue;
                    } else {
                        return Object.class;
                    }
                }

                return fail(thisClass, typeParamName);
            }
            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                return fail(thisClass, typeParamName);
            }
        }
    }

    private static int getTypeParamIndex(Class<?> parametrizedSuperclass, String typeParamName, TypeVariable<?>[] typeParams) {

        int typeParamIndex = -1;
        for (int i = 0; i < typeParams.length; i++) {
            if (typeParamName.equals(typeParams[i].getName())) {
                typeParamIndex = i;
                break;
            }
        }

        if (typeParamIndex < 0) {
            throw new IllegalStateException(
                    "unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);
        }
        return typeParamIndex;
    }


    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException(
                "cannot determine the type of the type parameter '" + typeParamName + "': " + type);
    }


    public static void main(String[] args) {
        class  MyMap extends  HashMap<String,Integer>{

        }
        MyMap myMap= new MyMap();
        System.out.println(find(myMap, Map.class, "K"));
        System.out.println(find(myMap, Map.class, "V"));
        System.out.println(find(myMap, HashMap.class, "V"));
        System.out.println(find(myMap, AbstractMap.class, "V"));



    }
}
