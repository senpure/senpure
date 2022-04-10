package com.senpure.executor;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;

import java.lang.reflect.Field;

/**
 * TRunable
 *
 * @author senpure
 * @time 2019-12-30 10:57:20
 */
public class TRunable implements Runnable {

    private Runnable runnable;
    InternalThreadLocalMap map;
    InternalThreadLocalMap current;
    Object v;
    int index;
    FastThreadLocal<?>[] variablesToRemoveArray;
    Object [] objects;
    public TRunable(Runnable runnable) {
        map = InternalThreadLocalMap.get();
        index = InternalThreadLocalMap.lastVariableIndex();
        v = map.indexedVariable(index);
        objects = new Object[index + 1];
        for (int i = 0; i < objects.length; i++) {
            Object object=map.indexedVariable(i);
            System.out.println(object.getClass());
            objects[i] = object;
        }
        System.out.println("index "+index);
//        if (v != null && v != InternalThreadLocalMap.UNSET) {
//            @SuppressWarnings("unchecked")
//            Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>) v;
//             variablesToRemoveArray =
//                    variablesToRemove.toArray(new FastThreadLocal[0]);
//            objects = new Object[variablesToRemoveArray.length];
//            System.arraycopy(variablesToRemoveArray,0,objects,0,variablesToRemoveArray.length);
//
//        }

        this.runnable = runnable;
        try {
            Field field=FastThreadLocal.class.getDeclaredField("variablesToRemoveIndex");
            field.setAccessible(true);
            Object value =field .get(null);
            System.out.println("variablesToRemoveIndex "+value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {




        InternalThreadLocalMap current = InternalThreadLocalMap.getIfSet();

        for (int i = 0; i < objects.length; i++) {
            current.setIndexedVariable(i,objects[i]);
        }


        runnable.run();

    }
}
