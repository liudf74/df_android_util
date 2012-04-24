package df.util.type;


import android.util.Log;

import java.io.BufferedInputStream;
import java.lang.reflect.*;
import java.sql.Blob;
import java.util.*;

/**
 * 一些通用的与对象相关的操作。
 */
public class ObjectUtil {

    public static final String TAG = "df.util.ObjectUtil";

    /**
     * 访问调用栈的顶层索引。
     * 使用Excalibure包中的scriptchpad中的StackIntrospector类。
     * 方法索引中，调用栈顶层的部分是StackIntrospector的内部信息，为2层；
     * 再加上调用getCallerMethodName()方法的一层，共需要减去3层。
     */
    private final static int INDEX_CALLER_METHOD = 3;

    //private static GeneralLogger theLogger = null; //LoggerFactory.getLogger( ObjectUtil.class.getName() );

    /*
    private static void logging( String message, Exception e )
    {
        getLogger().error( message, e );
    }
    */

    ///////////////////////////////////////////////////////////////////

    /**
     * 判别两个对象是否相同。
     */
    public static boolean equals(Object o1, Object o2) {
        return ((o1 == o2) || ((o1 != null) && (o1.equals(o2))));
    }

    /**
     * 获取对象的hash码。
     */
    public static int hashCode(Object o) {
        return ((null == o) ? 0 : o.hashCode());
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * 将数组转换为字符串。
     */
    public static String toArrayString(Object[] array) {
        if (null == array)
            return "count=0:[]";

        StringBuffer s = new StringBuffer("count=").append(array.length).append(":[ ");
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                s.append(", ");
            Object obj = array[i];
            if (null == obj)
                continue;
            else if (obj instanceof Object[])
                s.append(ObjectUtil.toString((Object[]) obj));
            else
                s.append(obj.toString());
        }
        s.append(" ]");
        return s.toString();
    }

    public static String toString(Object o) {
        if (null == o)
            return "";
        if (o instanceof Object[])
            return toArrayString((Object[]) o);
        else if (o instanceof int[])
            return NumberUtil.toString((int[]) o);
        else if (o instanceof long[])
            return NumberUtil.toString((long[]) o);
        else if (o instanceof char[])
            return StringUtil.toString((char[]) o);
        else if (o instanceof byte[])
            return DumpPackage.dumpPackage((byte[]) o);
        else if (o instanceof Calendar)
            return TimeUtil.toString((Calendar) o);
        else
            return o.toString();
    }

    public static String toClassFieldString(Object object) {
        if (object == null)
            return "";
        Class clazz = object.getClass();
        StringBuffer sValue = new StringBuffer();
        try {
            Field[] fields = clazz.getFields();
            StringBuffer sTmp = new StringBuffer();
            for (int i = 0; i < fields.length; i++) {
                sTmp.setLength(0);
                String sFieldType = fields[i].getType().getName();
                String sFieldName = fields[i].getName();

                if (sFieldType.startsWith("[L") && sFieldType.endsWith(";")) {
                    Object[] objects = (Object[]) fields[i].get(object);
                    if (null != objects) {
                        for (int j = 0; j < objects.length; j++) {
                            sTmp.append("{").append(sFieldName).append("[").append(j).append("]==")
                                    .append(objects[j] == null ? "" : objects[j].toString()).append("}");
                        }
                    }
                } else {
                    Object o = fields[i].get(object);
                    if (null != o)
                        sTmp.append(o.toString());
                }
                sValue.append("[").append(sFieldType).append(":").append(sFieldName).append("==").append(sTmp).append("]\r\n");
            }
        } catch (Exception e) {
            sValue.append("Error:").append(e.getMessage());
        }
        return sValue.toString();
    }

    ///////////////////////////////////////////////////////////////////
//
//    public static String toCallerClassName(Class clazz) {
//        String name = "";
//        Class caller = StackIntrospector.getCallerClass(clazz);
//        if (null != caller) {
//            name = caller.getName();
//            // 去掉开头的包名
//            //name = name.substring( name.lastIndexOf( "." ) + 1 );
//        }
//        return name;
//    }
//
//    public static String getCallerMethodName() {
//        String name = StackIntrospector.getCallerMethod(INDEX_CALLER_METHOD);
//        if (null != name) {
//            // 去掉结尾的参数
//            name = name.substring(0, name.indexOf("("));
//            // 去掉开头的包名
//            name = name.substring(name.lastIndexOf(".") + 1);
//        }
//        return name;
//    }

    ///////////////////////////////////////////////////////////////////

    public static String toBaseClassName(Object object) {
        if (null == object)
            return "";
        return getBaseClassName(object.getClass().getName());
    }

    /**
     * 获取不含有包名称的类名称
     */
    public static String getBaseClassName(String className) {
        return StringUtil.getLastSuffix(className, ".");
    }

    public static String getPackageName(String className) {
        return StringUtil.getLastPrefix(className, ".");
    }

    public static String getMethodName(String whole) {
        return StringUtil.getLastSuffix(whole, ".");
    }

    ///////////////////////////////////////////////////////////////////

    public static Object getProperty(Object object, String property) {
        if (null == object || null == property)
            return null;
        Class clazz;
        if (object instanceof Class) {
            clazz = (Class) object;
        } else {
            clazz = object.getClass();
        }
        try {
            Method method = clazz.getMethod("get" + StringUtil.upperFirst(property), (Class[]) null);
            return method.invoke(object, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, "get property " + property + " of " + clazz.getName() + "'s object failure", e);
        }
        return null;
    }

    public static Object[] getPropertyArray(Object object, String property) {
        if (null == object || null == property)
            return null;
        Class clazz;
        if (object instanceof Class) {
            clazz = (Class) object;
        } else {
            clazz = object.getClass();
        }
        try {
            String[] props = StringUtil.split2(property, ",");
            Object[] objs = new Object[props.length];
            for (int i = 0; i < objs.length; i++) {
                Method method = clazz.getMethod("get" + StringUtil.upperFirst(props[i]), (Class) null);
                objs[i] = method.invoke(object, (Class) null);
            }
            return objs;
        } catch (Exception e) {
            Log.e(TAG, "get property array " + property + " of " + clazz.getName() + "'s object failure", e);
        }
        return null;
    }

    public static Object getStaticProperty(Class clazz, String property) {
        String methodName = "get" + StringUtil.upperFirst(property);
        return getStaticMethod(clazz, methodName);
    }

    public static Object getStaticProperty(String className, String property) {
        String methodName = "get" + StringUtil.upperFirst(property);
        return getStaticMethod(className, methodName);
    }

    public static Object getStaticMethod(Class clazz, String methodName) {
        if (null == clazz || null == methodName)
            return null;
        try {
            String className = clazz.getName();
            Method method = clazz.getMethod(methodName, (Class[]) null);
            Object instance = newInstance(className, null);
            return method.invoke(instance, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, "get static method " + methodName + " of " + clazz.getName() + "'s clazz failure", e);
        }
        return null;
    }

    public static Object getStaticMethod(String className, String methodName) {
        if ((null == className) || (null == methodName))
            return null;
        try {
            Class clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, (Class[]) null);
            Object instance = newInstance(className, null);
            return method.invoke(instance, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, "get static method " + methodName + " of " + className + "'s clazz failure", e);
        }
        return null;
    }

    public static Object getPublicProperty(Object object, String property) {
        if (null == object || null == property)
            return null;
        try {
            //Class obj = Class.forName(object.getClass().getName());
            //Field field = obj.getField(property);
            Field field;
            property = property.trim();
            if (object instanceof Class)
                field = ((Class) object).getField(property);
            else
                field = object.getClass().getField(property);
            return field.get(object);
        } catch (Exception e) {
            Log.e(TAG, "get public property " + property + " of " + object.getClass().getName() + "'s object failure", e);
        }
        return null;
    }

    public static Object getPublicPropertyEx(Object object, String property) {
        if (null == object || null == property)
            return null;
        Class clazz = object instanceof Class ? (Class) object : object.getClass();
        Object value = null;
        while (null == (value = getPublicProperty(clazz, property))) {
            clazz = clazz.getSuperclass();
            if (null == clazz)
                break;
        }
        return value;
    }

    //public static Object getValueOfProperty(Object entity, String property) {
    //    if (entity == null || property == null)
    //        return null;
    //    try {
    //        property = property.trim();
    //        Field field = entity.getClass().getField(property);
    //        Object value = field.get(entity);
    //        return value;
    //    } catch (Exception e) {
    //        Log.e(TAG, "get  property " + property + " of " + entity.getClass().getName() + "'s object failure", e);
    //    }
    //
    //    return null;
    //}

    // Constant 即 public static final

    public static Object[] getConstantArrayByPrefixName(Object object, String prefixPropertyName) {
        if (null == object || null == prefixPropertyName)
            return null;
        try {
            List valueList = new ArrayList();
            Field[] fieldSet = null;
            if (object instanceof Class)
                fieldSet = ((Class) object).getFields();
            else
                fieldSet = object.getClass().getFields();
            for (int i = 0; i < fieldSet.length; i++) {
                Field field = fieldSet[i];
                if (field.getName().startsWith(prefixPropertyName)) {
                    try {
                        valueList.add(field.get(object));
                    } catch (Throwable e) {
                        Log.w(TAG, "get public final static field " + field.getName() + " of prefix name " + prefixPropertyName + " from object " + object.getClass().getName() + " failure", e);
                    }
                }
            }
            return valueList.toArray();
        } catch (Exception e) {
            Log.e(TAG, "get constant property of prefix name " + prefixPropertyName + " from object " + object.getClass().getName() + " failure", e);
        }
        return null;
    }

    // Constant 即 public static final
    public static Object getConstant(String className, String property) {
        if (StringUtil.empty(className))
            return null;
        try {
            Class clazz = Class.forName(className);
            Field[] fieldSet = (clazz).getFields();
            for (int i = 0; i < fieldSet.length; i++) {
                Field field = fieldSet[i];
                if (StringUtil.equals(field.getName(), property)) {
                    return field.get(clazz);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "get constant property for class name " + className + " failure", e);
        }
        return null;
    }

    /**
     *
     */
    public static void setStringProperty(Object object, String property, String value) {
        if (null == object || StringUtil.empty(property))
            return;

        Method method = null;
        try {
            method = object.getClass().getMethod("set" + StringUtil.upperFirst(property), new Class[]{new String().getClass()});
            method.invoke(object, new Object[]{value});
        } catch (Exception e) {
            Log.e(TAG, "set property of " + object.getClass().getName() + "'s propery " + property + " failure; value is: " + value, e);
        }
    }

    public static void setObjectProperty(Object object, String property, Object value) {
        if (null == object || StringUtil.empty(property))
            return;

        Method method = null;
        try {
            method = object.getClass().getMethod("set" + StringUtil.upperFirst(property), new Class[]{value.getClass()});
            method.invoke(object, new Object[]{value});
        } catch (Exception e) {
            Log.e(TAG, "set property of " + object.getClass().getName() + "'s propery " + property + " failure; value is: " + value, e);
        }
    }

    public static void setIntProperty(Object object, String property, int value) {
        if (null == object || StringUtil.empty(property))
            return;

        Method method = null;
        try {
            method = object.getClass().getMethod("set" + StringUtil.upperFirst(property), new Class[]{int.class});
            method.invoke(object, new Object[]{value});
        } catch (Exception e) {
            Log.e(TAG, "set property of " + object.getClass().getName() + "'s propery " + property + " failure; value is: " + value, e);
        }
    }

//    public static void setIntProperty(Object object, String property, int value) {
//        if (null == object || StringUtil.empty(property))
//            return;
//        try {
//            PropertyUtils.setSimpleProperty(object, property, new Integer(value));
//        } catch (Exception e) {
//            Log.e(TAG, "set property of " + object.getClass().getName() + "'s propery " + property + " failure; value is: " + value, e);
//        }
//    }


    public static Object invokeNewObjectMethod(String objectClassName, String methodName) {
        try {
            Class clazz = Class.forName(objectClassName);
            Object object = clazz.newInstance();
            Method method = clazz.getMethod(methodName, new Class[]{});
            return method.invoke(object, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, "invoke new object method failure, objectClassName = " + objectClassName + ", methodName = " + methodName, e);
            return null;
        }
    }

    public static Object invokeNewObjectMethodThrowException(String objectClassName, String methodName) throws Exception {
        try {
            Class clazz = Class.forName(objectClassName);
            Object object = clazz.newInstance();
            Method method = clazz.getMethod(methodName, new Class[]{});
            return method.invoke(object, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, "invoke new object method failure, objectClassName = " + objectClassName + ", methodName = " + methodName, e);
            throw e;
        }
    }

    public static Object invokeNewObjectMethod(String objectClassName, String methodName, Class[] argClassArray, Object[] argObjectArray) {
        try {
            Class clazz = Class.forName(objectClassName);
            Object object = clazz.newInstance();
            Method method = clazz.getMethod(methodName, argClassArray);
            return method.invoke(object, argObjectArray);
        } catch (Exception e) {
            Log.e(TAG, "invoke new object method failure" +
                    ", objectClassName = " + objectClassName +
                    ", methodName = " + methodName +
                    ", argClassArray = " + toString(argClassArray) +
                    ", argObjectArray = " + toString(argObjectArray),
                    e);
            return null;
        }
    }

    public static Object invokeNewObjectMethodThrowException(String objectClassName, String methodName, Class[] argClassArray, Object[] argObjectArray) throws Exception {
        try {
            Class clazz = Class.forName(objectClassName);
            Object object = clazz.newInstance();
            Method method = clazz.getMethod(methodName, argClassArray);
            return method.invoke(object, argObjectArray);
        } catch (Exception e) {
            Log.e(TAG, "invoke new object method failure" +
                    ", objectClassName = " + objectClassName +
                    ", methodName = " + methodName +
                    ", argClassArray = " + toString(argClassArray) +
                    ", argObjectArray = " + toString(argObjectArray),
                    e);
            throw e;
        }
    }

    public static Object newInstance(String objectClassName, Object constructorArgObject) {
        /*
        try {
            Class _class = Class.forName( objectClassName );
            if ( null == constructorArgObject ) {
                return _class.newInstance();
            } else {
                Object[] intArgs = new Object[]{constructorArgObject};
                Constructor intArgsConstructor = _class.getConstructor( new Class[]{constructorArgObject.getClass()} );
                return intArgsConstructor.newInstance( intArgs );
            }
        } catch ( Exception e ) {
            Log.e(TAG,  "new instance failure, objectClassName = " + objectClassName + ", constructorArgObject = " + constructorArgObject, e );
            return null;
        }
        */
        if (constructorArgObject == null)
            return newInstance(objectClassName, constructorArgObject, null);
        return newInstance(objectClassName, constructorArgObject, constructorArgObject.getClass());
    }

    public static Object newInstance(String objectClassName, Object constructorArgObject, Class constructorArgClass) {
        try {
            Class _class = Class.forName(objectClassName);
            if (null == constructorArgObject) {
                return _class.newInstance();
            } else {
                Object[] intArgs = new Object[]{constructorArgObject};
                Constructor intArgsConstructor = _class.getConstructor(new Class[]{constructorArgClass});
                return intArgsConstructor.newInstance(intArgs);
            }
        } catch (Exception e) {
            Log.e(TAG, "new instance failure, objectClassName = " + objectClassName + ", constructorArgObject = " + constructorArgObject, e);
            return null;
        }
    }

    public static Object newInstance(String className, Class[] clazzs, Object[] args) {
        try {
            Class _class = Class.forName(className);
            if (null == clazzs) {
                return _class.newInstance();
            } else {
                Constructor intArgsConstructor = _class.getConstructor(clazzs);
                return intArgsConstructor.newInstance(args);
            }
        } catch (Exception e) {
            Log.e(TAG, "new instance failure, objectClassName = " + className + ", args = " + args, e);
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////

    public static StringBuffer getClassAsString(Class clazz, Object object) {
        StringBuffer results = new StringBuffer();
        results.append(clazz.getName() + "[ ");

        Field[] fields = clazz.getDeclaredFields();
        try {
            AccessibleObject.setAccessible(fields, true);
            if (fields.length > 0) {
                boolean appended = appendFieldString(results, fields[0], object);
                for (int i = 1; i < fields.length; i++) {
                    if (appended)
                        results.append(", ");
                    appended = appendFieldString(results, fields[i], object);
                }
            }
        } catch (Exception e) {
            // ignored!
        }

        results.append(" ] ");
        return results;
    }

    public static Class getPropertyType(Class owner, String fieldName) {
        try {
            Field field = owner.getDeclaredField(fieldName);
            return field.getType();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean appendFieldString(StringBuffer results, Field field, Object object) throws IllegalAccessException {
        final int modifiers = field.getModifiers();
        if ((Modifier.isFinal(modifiers)) || (Modifier.isStatic(modifiers)))
            return false;
        results.append(field.getName());
        results.append("=");
        results.append(toString(field.get(object)));
        return true;
    }

    public static String getObjectAsString(Object object) {
        StringBuffer results = new StringBuffer();

        // 不显示以本类所在的util包下面的所有类
        String PKG = ".util.";
        String className = ObjectUtil.class.getName();
        int index = className.indexOf(PKG);
        String pkgPrefix = className.substring(0, index + PKG.length());
        if (object != null) {
            for (Class clazz = object.getClass(); (clazz != null) && (clazz.getName().startsWith(pkgPrefix)); clazz = clazz.getSuperclass()) {
                results.append(getClassAsString(clazz, object));
            }
        }

        return results.toString();
    }

//    public static void closeOutputStream(OutputStream os) {
//        FileUtil.close(os);
//        //try {
//        //    if ( null != os )
//        //        os.close();
//        //} catch ( IOException e ) {
//        //    Log.e(TAG,  "close outputstream failure", e );
//        //}
//    }
//
//    public static void closeInputStream(InputStream is) {
//        FileUtil.close(is);
//        //try {
//        //    if ( null != is )
//        //        is.close();
//        //} catch ( IOException e ) {
//        //    Log.e(TAG,  "close inputstream failure", e );
//        //}
//    }

//    /**
//     * 拷贝对象属性，不抛出异常。
//     *
//     * @param dest
//     * @param src
//     */
//    public static void copyPropertiesSafe(Object dest, Object src) {
//        if ((null == src) || (null == dest))
//            return;
//
//        try {
//            PropertyUtils.copyProperties(dest, src);
//        } catch (Throwable e) {
//            String hintHead = "copyPropertiesSafe";
//            Log.e(TAG, hintHead + ", copy properties failure", e);
//            if (Log.isDebugEnabled()) {
//                Log.debug(hintHead + ", dest = " + dest);
//                Log.debug(hintHead + ", src = " + src);
//            }
//        }
//    }

    /**
     * 获取类的域对象，并一直上朔所有父类，直到找到位置。
     *
     * @param clazz
     * @param name
     * @return
     */
    public static Field getDeclaredField(Class clazz, String name) {
        Field field = null;
        while (null == field) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (Exception e) {
                try {
                    clazz = clazz.getSuperclass();
                    if (clazz == null)
                        return null;
                } catch (Exception e1) {
                    return null;
                }
            }
        }
        return field;
    }

    public static Object getCollectionIndexObject(Collection collection, int index) {
        int i = 0;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (index == i) {
                return obj;
            }
            i++;
        }
        return null;
    }


    static public byte[] getBytesOfBlob(final Blob blob) {
        byte[] bytes = null;

        try {
            if (blob != null) {
                bytes = new byte[(int) blob.length()];
                BufferedInputStream bi = new BufferedInputStream(blob.getBinaryStream());
                int j = 0;
                int i = bi.read();
                while (i != -1) {
                    bytes[j++] = (byte) i;
                    i = bi.read();
                }
                bi.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "read blob to byte[] fail.", e);
        }

        return bytes;
    }
}
