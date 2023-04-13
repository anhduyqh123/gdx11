package GDX11;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.reflect.*;

import java.util.*;

public class Reflect {
    private static final Map<Class,Object> typeToDefaultObject = new HashMap<>();
    private static final Map<Class,Map<String,Field>> allField = new HashMap<>();
    private static final Map<Class,Map<String,Field>> dataField = new HashMap<>();
    private static Map<Object,Map<Object, GDX.Runnable1<Object>>> event = new HashMap<>();
    public static void AddEvent(Object object,Object field,GDX.Runnable1<Object> cb)
    {
        if (event.get(object)==null) event.put(object,new HashMap<>());
        event.get(object).put(field,cb);
    }
    public static void OnChange(Object object)
    {
        if (event.get(object)==null) return;
        for (Object field : event.get(object).keySet())
            OnChange(object,field);
    }
    private static void OnChange(Object object,Object field)
    {
        if (event.get(object)==null || event.get(object).get(field)==null) return;
        Object value = field instanceof Field?GetValue((Field) field,object):null;
        event.get(object).get(field).Run(value);
    }

    public static boolean IsAssignableFrom(Class type, Class superClass)
    {
        return ClassReflection.isAssignableFrom(superClass,type);
    }
    public static boolean equals(Object ob1,Object ob2) //for class
    {
        if (ob1==null && ob2==null) return true;
        if (ob1!=null && ob2!=null) return EqualsFields(ob1,ob2);
        return false;
    }
    public static boolean Equals(Object objectA,Object objectB)
    {
        if (objectA==null && objectB==null) return true;
        if (objectA!=null && objectB!=null) return objectA.equals(objectB);
        return false;
    }
    private static boolean EqualsFields(Object ob1,Object ob2)
    {
        Class type1 = ob1.getClass();
        Class type2 = ob2.getClass();
        if (!type1.equals(type2)) return false;
        for (Field f : GetDataFieldMap(type1).values())
        {
            Object e1 = GetValue(f,ob1);
            Object e2 = GetValue(f,ob2);
            if (!e1.equals(e2)) return false;
        }
        return true;
    }

    //Field
    public static void SetValue(Field field,Object object,Object value)
    {
        try {
            field.set(object,value);
        }catch (Exception e){}
    }
    public static void SetValue(String filedName,Object object,Object value)
    {
        SetValue(GetField(object.getClass(),filedName),object,value);
    }
    public static <T> T GetValue(Field field,Object object)
    {
        try {
            return (T)field.get(object);
        }catch (Exception e){}
        return null;
    }
    public static <T> T GetValue(String filedName,Object object)
    {
        return GetValue(GetField(object.getClass(),filedName),object);
    }
    public static Field GetField(Class type,String fieldName) //all field but only public
    {
        return GetAllFieldMap(type).get(fieldName);
    }
    public static Field GetDeclaredField(Class type,String fieldName) //only local field
    {
        try {
            return ClassReflection.getDeclaredField(type,fieldName);
        }catch (Exception e){}
        return null;
    }
    public static Field[] GetDeclaredFields(Class type) //only local field
    {
        try {
            return ClassReflection.getDeclaredFields(type);
        }catch (Exception e){}
        return null;
    }
    private static List<Field> GetAllFields(Class type) //All Field
    {
        Array<Class> classHierarchy = new Array();
        Class nextClass = type;
        while (nextClass != Object.class) {
            classHierarchy.add(nextClass);
            nextClass = nextClass.getSuperclass();
        }
        ArrayList<Field> allFields = new ArrayList();
        for (int i = classHierarchy.size - 1; i >= 0; i--)
            Collections.addAll(allFields, ClassReflection.getDeclaredFields(classHierarchy.get(i)));
        return allFields;
    }
    public static Map<String, Field> GetFields(Class type,List<String> fieldNames)
    {
        Map<String, Field> map = new HashMap<>();
        Map<String, Field> fields = GetAllFieldMap(type);
        for(String name : fieldNames)
            map.put(name,fields.get(name));
        return map;
    }
    public static Map<String, Field> GetAllFieldMap(Class type)
    {
        if (allField.containsKey(type)) return allField.get(type);
        Map<String, Field> map = new HashMap<>();
        for(Field f : GetAllFields(type))
        {
            if (!f.isAccessible()) f.setAccessible(true);
            map.put(f.getName(),f);
        }
        allField.put(type,map);
        return map;
    }
    public static Map<String, Field> GetDataFieldMap(Class type)
    {
        if (dataField.containsKey(type)) return dataField.get(type);
        Map<String, Field> map = new LinkedHashMap<>();
        for(Field f : GetAllFieldMap(type).values())
            if (IsDataField(f)) map.put(f.getName(),f);
        dataField.put(type,map);
        return map;
    }
    //Method
    public static Method GetMethod(Class type, String methodName, Class... parameterTypes) //all field but only public
    {
        try {
            return ClassReflection.getMethod(type,methodName,parameterTypes);
        }catch (Exception e){}
        return null;
    }
    public static <T> T RunMethod(Method method,Object object,Object... args)
    {
        try {
            return (T)method.invoke(object,args);
        }catch (Exception e){}
        return null;
    }
    public static Object GetDefaultObject(Class type)
    {
        if (typeToDefaultObject.containsKey(type)) return typeToDefaultObject.get(type);
        Object object = NewInstance(type);
        typeToDefaultObject.put(type,object);
        return object;
    }
    public static Class GetClass(String name)
    {
        try {
            return ClassReflection.forName(name);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public static boolean IsDataField(Field field)
    {
        if (field.isTransient()) return false;//new
        if (field.isSynthetic()) return false;//new
        if (field.isStatic()) return false;
        if (!IsDataClass(field.getType())) return false;
        return true;
    }
    public static boolean IsDataClass(Class type)
    {
        if (type == Map.class || type == List.class) return true;
        if (type.isInterface()) return false;
        if (type.isAnonymousClass()) return false;//override class
        return true;
    }

    public static <T> T NewInstance (Class type) {
        if (IsBaseType(type)) return (T)NewBaseType(type);
        try {
            if (ClassReflection.isAssignableFrom(Enum.class, type)) {
                if (type.getEnumConstants() == null) type = type.getSuperclass();
                return (T)type.getEnumConstants()[0];
            }
            return (T)ClassReflection.newInstance(type);
        } catch (Exception ex) {
            try {
                // Try a private constructor.
                Constructor constructor = ClassReflection.getDeclaredConstructor(type);
                constructor.setAccessible(true);
                return (T)constructor.newInstance();
            } catch (SecurityException ignored) {
            } catch (ReflectionException ignored) {
                if (type.isArray())
                    throw new SerializationException("Encountered JSON object when expected array of type: " + type.getName(), ex);
                else if (ClassReflection.isMemberClass(type) && !ClassReflection.isStaticClass(type))
                    throw new SerializationException("Class cannot be created (non-static member class): " + type.getName(), ex);
                else
                    throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex);
            } catch (Exception privateConstructorException) {
                ex = privateConstructorException;
            }
            throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
        }
    }
    //base type
    public static boolean IsBaseType(Class actualType)
    {
        return actualType.isPrimitive() || actualType == String.class || actualType == Integer.class || actualType == Boolean.class
                || actualType == Float.class || actualType == Long.class || actualType == Double.class || actualType == Short.class
                || actualType == Byte.class || actualType == Character.class;
    }
    public static <T> Object NewBaseType(Class<T> type)
    {
        if (type==int.class || type == Integer.class) return 0;
        if (type==float.class || type == Float.class) return 0;
        if (type==long.class || type == Long.class) return 0;
        if (type==double.class || type == Double.class) return 0;
        if (type==short.class || type == Short.class) return 0;
        if (type==byte.class || type == Byte.class) return 0;
        if (type == String.class) return "";
        if (type==boolean.class || type == Boolean.class) return false;
        if (type==char.class || type == Character.class) return '0';
        if (type == CharSequence.class) return '0';
        return null;
    }
    public static <T> T ToBaseType(String stValue, Class type)
    {
        if (type==int.class || type == Integer.class) return (T)Integer.valueOf(stValue);
        if (type==float.class || type == Float.class) return (T)Float.valueOf(stValue);
        if (type==long.class || type == Long.class) return (T)Long.valueOf(stValue);
        if (type==double.class || type == Double.class) return (T)Double.valueOf(stValue);
        if (type==short.class || type == Short.class) return (T)Short.valueOf(stValue);
        if (type==byte.class || type == Byte.class) return (T)Byte.valueOf(stValue);
        if (type == String.class) return (T)stValue;
        if (type==boolean.class || type == Boolean.class) return (T)Boolean.valueOf(stValue);
        if (type==char.class || type == Character.class) return (T)(Character)stValue.charAt(0);
        if (type == CharSequence.class) return (T)stValue;
        return null;
    }
    public static  <T> T ToBaseType(String stValue,T value0)
    {
        return GDX.Try(()->(T) ToBaseType(stValue,value0.getClass()),()->value0);
    }

    //<editor-fold desc="Clone">
    public static <T> T Clone(Object object)
    {
        return (T)Clone(object,object.getClass());
    }
    public static <T> T Clone(Object object,Class<T> type)
    {
        if (IsBaseType(object.getClass())) return (T)object;
        return Clone(NewInstance(type),object);
    }
    private static <T> T Clone(Object object,Object objectClone)
    {
        if (objectClone==null) return null;
        object = NewObject(object,objectClone);
        if (IsBaseType(object.getClass())) return (T)objectClone;
        if (object instanceof Enum) return (T)objectClone;
        if (object instanceof List) return (T)Clone((List) object,(List) objectClone);
        if (object instanceof Map) return (T)Clone((Map) object,(Map) objectClone);
        CloneFields(object,objectClone);
        return (T)object;
    }
    private static Object NewObject(Object object,Object objectClone)
    {
        if (object==null || !object.getClass().equals(objectClone.getClass()))
            return NewInstance(objectClone.getClass());
        return object;
    }
    private static void CloneFields(Object object,Object objectClone)
    {
        for(Field f : GetDataFieldMap(object.getClass()).values())
        {
            Object value = GetValue(f,object);
            Object value0 = GetValue(f,objectClone);
            if (Equals(value,value0)) continue;
            SetValue(f,object,Clone(value,value0));
        }
    }

    private static List Clone(List list,List listClone)
    {
        list.clear();
        for (Object object : listClone)
            list.add(Clone(object));
        return list;
    }
    private static Map Clone(Map map,Map mapClone)
    {
        map.clear();
        for(Object key : mapClone.keySet())
            map.put(key,Clone(mapClone.get(key)));
        return map;
    }
    //</editor-fold>
}
