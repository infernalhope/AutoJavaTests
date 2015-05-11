
package testmanager;

import exceptions.CodeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import logger.TestLogger;
import test.TestHandler;


public class ObjectExplorer
{
    public enum type
    {
        CLASS,
        METHOD,
        FIELD;
    }
    
    public static Object invokeStatic(String methodName, Object... methodParams) 
    {
        Object ret = null;
        List<Class> studentClasses = TestHandler.getStudentClasses();
        
        
        for(Class c : studentClasses)
        {
            Method[] methods = c.getDeclaredMethods();
            
            for(Method m : methods)
            {
                if(m.getName().contains(methodName))
                {
                    m.setAccessible(true);
                    
                    try 
                    {
                        ret = m.invoke(null, methodParams);
                    } 
                    catch (IllegalAccessException ex) 
                    {
                        TestLogger.setResult("Test error");
                        String message = "Statická metoda není přístupná: " +methodName;
                        TestLogger.addResultMessage(message);
                        TestHandler.setSkipAfterFatalErrorLog(true);
                    } 
                    catch (IllegalArgumentException ex) 
                    {
                        TestLogger.setResult("Test error");
                        String message = "Špatná instance třídy nebo nesedí vstupní parametry pro statickou metodu: " +methodName;
                        TestLogger.addResultMessage(message);
                        TestHandler.setSkipAfterFatalErrorLog(true);
                    } 
                    catch (InvocationTargetException ex) 
                    {
                        TestLogger.setResult("Test error");
                        String message = "Statická metoda vyvolala výjimku: " +methodName;
                        TestLogger.addResultMessage(message);
                        TestHandler.setSkipAfterFatalErrorLog(true);
                    }
                }
            }
        }

        return ret;
    }

    public static Object invoke(Object studentClassInstance, String methodName, Object... methodParams) 
    {
        Object ret = 0;
        Method studentMethod = null;
        
        try 
        {
            Class[] params = getParamTypes(studentClassInstance.getClass(), methodName);

            studentMethod = studentClassInstance.getClass().getMethod(methodName, params);
            studentMethod.setAccessible(true);

            ret = studentMethod.invoke(studentClassInstance, methodParams);
            
        } 
        catch (IllegalAccessException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Metoda není přístupná: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        }
        catch (IllegalArgumentException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Špatná instance třídy nebo nesedí vstupní parametry pro metodu: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        }  
        catch (InvocationTargetException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Metoda vyvolala výjimku: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        }
        catch (NoSuchMethodException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Metoda neexistuje: " +methodName;
            TestHandler.setSkipAfterFatalErrorLog(true);
        } 
        catch (SecurityException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "SecurityManager odepřel přístup k metodě: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);  
        }
        
        return ret;
    }
    
    public static Throwable invokeMethodException(Object studentClassInstance, String methodName, Object... methodParams) 
    {
        Throwable ret = new Throwable();
        Method studentMethod = null;
        
        try 
        {
            Class[] params = getParamTypes(studentClassInstance.getClass(), methodName);

            studentMethod = studentClassInstance.getClass().getMethod(methodName, params);
            studentMethod.setAccessible(true);

            studentMethod.invoke(studentClassInstance, methodParams);
            
        } 
        catch (IllegalAccessException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Metoda není přístupná: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        }
        catch (IllegalArgumentException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Špatná instance třídy nebo nesedí vstupní parametry pro metodu: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        }  
        catch (InvocationTargetException ex) 
        {
            ret = ex;
        }
        catch (NoSuchMethodException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Metoda neexistuje: " +methodName;
            TestHandler.setSkipAfterFatalErrorLog(true);
        } 
        catch (SecurityException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "SecurityManager odepřel přístup k metodě: " +methodName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);  
        }
        
        return ret;
    }

    public static Object getFieldValue(Object studentClassInstance, String fieldName)
    {
        Object ret = new Object();
        try 
        {

            Field[] studentFields = studentClassInstance.getClass().getDeclaredFields();
            for(Field field : studentFields)
            {
                if(field.getName().equals(fieldName))
                {
                    field.setAccessible(true);
                    ret = field.get(ret);
                }
            }
        } 
        catch (IllegalArgumentException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Proměnná neexistuje: " +fieldName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        } 
        catch (Exception ex) 
        {
            new CodeException("Chyba při y9sk8v8n9 hodnotz y po6adovan0 prom2nn0: " +fieldName, ex);
        }

        return ret;
    }
    
    public static Object setFieldValue(Object studentClassInstance, String fieldName, Object value)
    {
        Object ret = studentClassInstance;
        
        try 
        {
            Field[] studentFields = studentClassInstance.getClass().getDeclaredFields();
            for(Field field : studentFields)
            {
                if(field.getName().equals(fieldName))
                {
                    field.setAccessible(true);
                    field.set(ret, value); 
                }
            }
             
        }
        catch (IllegalArgumentException ex) 
        {
            TestLogger.setResult("Test error");
            String message = "Proměnná neexistuje: " +fieldName;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
        } 
        catch (Exception ex) 
        {
            new CodeException("Chyba při y9sk8v8n9 hodnotz y po6adovan0 prom2nn0: " +fieldName, ex);
        }
        
        return ret;
    }
    
    public static boolean isFinal(Object instance, String name, type type) 
    {
        int modifier = getModifier(instance, name, type);
        return Modifier.isFinal(modifier);
    }

    public static boolean fieldExists(Object object, String fieldName) 
    {
        boolean ret = false;
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return ret;
    }

    public static boolean isAbstract(Object instance, String name, type type) 
    {
        int modifier = getModifier(instance, name, type);
        return Modifier.isAbstract(modifier);
    }

    public static boolean isSuccessorOf(Class successor, Class ancestor) 
    {
        boolean ret = false;
        
        if(ancestor.isAssignableFrom(successor))
        {
            ret = true;
        }
 
        return ret;
    }
    
    public static boolean typesEqual(Class first, Class second)
    {
        boolean ret = false;
        
        if(isSuccessorOf(second, first))
        {
            if(isSuccessorOf(first, second))
            {
                ret = true;
            }
        }
        
        return ret;
    }

    public static boolean isStatic(Object instance, String name, type type) 
    {
        int modifier = getModifier(instance, name, type);
        return Modifier.isStatic(modifier);
    }

    public static boolean methodExists(Object object, String methodName) 
    {
        boolean ret = false;
        Class clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return true;
            }
        }
        return ret;
    }

    public static boolean isPublic(Object instance, String name, type type) 
    {
        int modifier = getModifier(instance, name, type);
        return Modifier.isPublic(modifier);
    }

    public static boolean isPrivate(Object instance, String name, type type) 
    {
        int modifier = getModifier(instance, name, type);
        return Modifier.isPrivate(modifier);
    }
     
    private static Class[] getParamTypes(Class studentClassInstance, String methodName) 
    {
        Class[] ret = null;
        Method[] methods = studentClassInstance.getDeclaredMethods();
        
        for(Method m : methods)
        {
            if(m.getName().contains(methodName)) return m.getParameterTypes();
        }
        
        return ret;
    }
    
    private static int getModifier(Object instance, String name, type type) 
    {
        int ret = -1;
        if (type.equals(type.CLASS)) {
            ret = instance.getClass().getModifiers();
        } else if (type.equals(type.METHOD)) {
            Method[] methods = instance.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals(name)) {
                    ret = m.getModifiers();
                    break;
                }
            }
        } else if (type.equals(type.FIELD)) {
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals(name)) {
                    ret = f.getModifiers();
                    break;
                }
            }
        }
        return ret;
    }
    
}

