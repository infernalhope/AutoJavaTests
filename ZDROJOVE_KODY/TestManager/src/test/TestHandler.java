
package test;

import annotations.InjectInstanceOfClass;
import codeHandlers.ClassHandler;
import exceptions.CodeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import logger.TestLogger;

public class TestHandler 
{
    private static List<Class> teacherClasses;
    private static List<Class> studentClasses;
    private static String testMethodName = "";
    private static boolean skipAfterFatalErrorLog;
    
    public static void prepareTest(List<String> teacherJarFiles, List<String> studentJarFiles)
    {
        try
        {
        ClassHandler.initURLClassLoader(teacherJarFiles, studentJarFiles);

        teacherClasses = ClassHandler.getClassesList(teacherJarFiles);
        studentClasses = ClassHandler.getClassesList(studentJarFiles);
        setTestMethodName("");
        
        startTest(teacherClasses);
        }
        catch(CodeException ex)
        {
            TestLogger.logResult();
            TestLogger.resetResultInfo();
            TestLogger.resetResult();
        }
    }

    public static List<Class> getTeacherClasses() 
    {
        return teacherClasses;
    }

    public static List<Class> getStudentClasses() 
    {
        return studentClasses;
    }

    private static void startTest(List<Class> teacherClasses) 
    {
        for(Class c : teacherClasses)
        {
            if(c.isInterface()) continue;
            
            
            Method[] methods = c.getDeclaredMethods();

            for(Method m : methods)
            {
                InjectInstanceOfClass classAnnotation = m.getAnnotation(InjectInstanceOfClass.class);
                if(classAnnotation == null) continue;
                setSkipAfterFatalErrorLog(false);
                
                try 
                {
                    setTestMethodName(m.getName());
                    
                    Class[] param = m.getParameterTypes();
                    String className = classAnnotation.classNameRegex();
                    Object studentInstance;

                    if(param.length != 0)
                    {
                        studentInstance = ClassHandler.getImplementInstance(m, className, studentClasses);
                        m.setAccessible(true); 
                        m.invoke(c.newInstance(), studentInstance);
                    }
                    else
                    {
                        m.setAccessible(true); 
                        m.invoke(c.newInstance());
                    }
                } 
                catch (Exception ex) 
                {
                    if(isSkipAfterFatalErrorLog() == false)
                    {
                        TestLogger.setResult("Fatal error");
                        TestLogger.addResultMessage("Testovací metoda vyvovala výjimku: " 
                                +ex.getCause().getClass() +" || "
                                +ex.getCause().getMessage());
                    }
                }
                
                TestLogger.logResult();
                TestLogger.resetResultInfo();
                TestLogger.resetResult();
                
            }//for(Method m : methods)
        }//for(Class c : teacherClasses)
    }
    
    public static String getTestMethodName() 
    {
        return testMethodName;
    }

    public static void setTestMethodName(String testMethodName) 
    {
        TestHandler.testMethodName = testMethodName;
    }

    public static boolean isSkipAfterFatalErrorLog() 
    {
        return skipAfterFatalErrorLog;
    }

    public static void setSkipAfterFatalErrorLog(boolean skipFatalErrorLog) 
    {
        TestHandler.skipAfterFatalErrorLog = skipFatalErrorLog;
    }
    
    

}
