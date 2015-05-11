
package codeHandlers;

import exceptions.CodeException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import logger.TestLogger;
import test.TestHandler;

public class ClassHandler 
{

    private static URLClassLoader urlcl;
    public static void initURLClassLoader(List<String> teacherJarFiles, List<String> studentJarFiles) throws CodeException
    {
        try 
        {
            List<String> jarFiles = new ArrayList<>();
            jarFiles.addAll(teacherJarFiles);
            jarFiles.addAll(teacherJarFiles.size(), studentJarFiles);
            URL[] myURLs = new URL[jarFiles.size()];
            int index = 0;
            
            for(String url : jarFiles)
            {
                myURLs[index] = new URL("jar:file:" + url + "!/");
                index++;
            }
            
            urlcl = URLClassLoader.newInstance(myURLs);

        } 
        catch (MalformedURLException ex) 
        {
            throw new CodeException("Chyba při vytváření URLClassLoaderu: ", ex);
        }
    }
    
    public static List<Class> getClassesList(List<String> jarFiles) throws CodeException
    {
         List<Class> ret = new ArrayList<>();
         String jarFileNameToLog = "none";
         String classNameToLog = "none";
         try 
         {
         
             for(String jarFile : jarFiles)
             {
                 jarFileNameToLog = jarFile.substring(jarFile.lastIndexOf("\\"), jarFile.length());
                 List<String> classNames = getClassNamesFromJar(jarFile);
                 classNames = getFullClassNames(classNames);

                 for(String className : classNames)
                 {
                     classNameToLog = className;
                     ret.add(urlcl.loadClass(className));       
                 }
                 
             }
         }
         catch(Throwable ex) 
         {
            TestLogger.setResult("Test error");
            String message = "Chyba při vytváření instance třídy '" +classNameToLog+"' z jar: " +jarFileNameToLog;
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
            throw new CodeException("Chyba při vytváření instance třídy '" +classNameToLog+"' z jar: " +jarFileNameToLog, ex);
         }
         
         return ret;       
    }
    
    public static List<String> getClassNamesFromJar(String jarPath) throws Exception
    {
        List<String> ret = new ArrayList<>();
        ZipFile file = null;
        try 
        {         
            file = new ZipFile(jarPath, Charset.forName("CP852"));
            if (file != null) 
            {
                Enumeration<? extends ZipEntry> entries = file.entries();
                
                if (entries != null) 
                {
                    while (entries.hasMoreElements())
                    {
                        ZipEntry entry = entries.nextElement();                     
                        if(entry.getName().endsWith(".class"))
                        {
                            ret.add(entry.getName());
                        }
                    }
                    
                }
            }   
        } 
        catch (IOException IOex) 
        {
            try 
            {
                file.close();
            } 
            catch (Exception ex) 
            {
                IOex.initCause(new Exception("nelze provést (ZipFile)file.close()"));
            }
            throw new Exception(IOex);
        }
        finally
        {
            file.close();
        }
        
       return ret; 
    }
    
    public static List<String> getFullClassNames(List<String> output) 
    {
        List<String> ret = new ArrayList<>();
        
        for(String s : output)
        {
               s = s.replaceAll("/", "."); // org/demo/Trida.class ---> org.demo.Trida.class
               s = s.substring(0, (s.length()-6)); // org.demo.Trida.class ---> org.demo.Trida
               ret.add(s);
        }

        return ret;
    }  

    public static Object getImplementInstance(Method method, String classNameToReturn, List<Class> classes) throws CodeException 
    {
        Class[] params = method.getParameterTypes();
     
        for(Class clazz : classes)
        {
            Class param = params[0];

            if(!classNameToReturn.isEmpty())
            {
                Pattern p = Pattern.compile(classNameToReturn);
                Matcher m = p.matcher(clazz.getName());
                boolean matches = m.matches();
                
                if(param.isAssignableFrom(clazz) && matches)
                {
                    try 
                    {
                        return clazz.newInstance();
                    } 
                    catch (InstantiationException ex) 
                    {
                        TestLogger.setResult("Test error");
                        String message = "Třída " +clazz.getName() +" nemá bezparametrický konstruktor";
                        TestLogger.addResultMessage(message);
                        TestHandler.setSkipAfterFatalErrorLog(true);
                        new CodeException("Chyba při vytváření instance třídy: " +clazz.getName(), ex);
                    } 
                    catch (IllegalAccessException ex) 
                    {
                        new CodeException("Chyba při vytváření instance třídy: " +clazz.getName(), ex);
                    } 
                }
            }
            else
            {
                if(param.isAssignableFrom(clazz))
                {
                    try 
                    {
                        return clazz.newInstance();
                    } 
                    catch (InstantiationException ex) 
                    {
                        TestLogger.setResult("Test error");
                        String message = "Třída " +clazz.getName() +" nemá bezparametrický konstruktor";
                        TestLogger.addResultMessage(message);
                        TestHandler.setSkipAfterFatalErrorLog(true);
                        new CodeException("Chyba při vytváření instance třídy: " +clazz.getName(), ex);
                    } 
                    catch (IllegalAccessException ex) 
                    {
                        new CodeException("Chyba při vytváření instance třídy: " +clazz.getName(), ex);
                    } 
                }
            }
        }
        if(!TestHandler.isSkipAfterFatalErrorLog())
        {
            TestLogger.setResult("Test error");
            TestLogger.addResultMessage("Třída neexistuje: " +classNameToReturn);
            TestHandler.setSkipAfterFatalErrorLog(true);
            throw new CodeException();
        }
        else
        {
            throw new CodeException();
        }
    }       
}
