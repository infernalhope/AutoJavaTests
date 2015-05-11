
package logger;

import codeHandlers.ZipHandler;
import exceptions.CodeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CodeLogger
{
    private static final String newLine = System.getProperty("line.separator");
    private static boolean logAppended = false;
    
    public static void logException(String message) 
    {
        StringBuilder sb = new StringBuilder();
        setLogAppended(true);
        
        sb.append("___________________");
        sb.append(ZipHandler.getUser());
        sb.append("___________________");
        sb.append(newLine);
        sb.append(message);
        sb.append(newLine);
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy, HH:mm:ss");
        String date = format.format(c.getTime());
        sb.append(date);
        
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);

        try 
        {
            File log = getLogFileReference();
            logIt(log, sb.toString());
        } 
        catch (IOException ex) 
        {
            Path newLogFile = Paths.get(System.getProperty("java.io.tmpdir")+"//__ErrorLog.txt");
             try 
             {
                 if(!Files.exists(newLogFile))
                 {
                     Files.createFile(newLogFile);
                 }

                 sb.append(ex.getMessage());
                 sb.append(newLine);
                 sb.append("Nepodařil se vytvořit ErrorLog.txt !!");
                 sb.append(newLine);
                 logIt(newLogFile.toFile(), sb.toString());
             } 
             catch (IOException ex1) 
             {
                 System.out.println("Selhalo vytvoření 'ErrorLog.txt' i '__ErrorLog.txt' \n" +ex1.getMessage());
             }
        }
    }
    
    private static File getLogFileReference() throws IOException 
    {
        File f = new File(ZipHandler.getTestFolder());
        LogFileFilter lff = new LogFileFilter();
        String logName = "\\ErrorLog.txt";
        
        File[] files = f.listFiles(lff);
        
        if(files.length == 0 || f.getName().equalsIgnoreCase("temp"))
        {          
            return createLog(logName, f.toPath());      
        }
        else
        {
            for(File file : files)
            {
                if(file.getName().contains(logName.substring(2, logName.length())))
                {
                    return file;
                }
            }
            
            return createLog(logName, f.toPath());
        }
    }

    private static File createLog(String logName, Path path) throws IOException 
    {
        Path logFile = Paths.get(path.toString()+logName);
        
        Files.createFile(logFile); 
        
        return new File(logFile.toString());
    }

    private static void logIt(File log, String message) throws IOException
    {
       byte[] buffer = message.getBytes();
       
       Files.write(log.toPath(), buffer, WRITE, APPEND);
    }

    public static boolean isLogAppended() 
    {
        return logAppended;
    }

    public static void setLogAppended(boolean logAppended) 
    {
        CodeLogger.logAppended = logAppended;
    }
    
    public static void addEndLine()
    {
        StringBuilder sb = new StringBuilder();
        String line = "***************************************************";
        sb.append(line);
        sb.append(newLine);
        sb.append(line);
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);
        sb.append(newLine);
        
        try 
        {
            logIt(getLogFileReference(), sb.toString());
        } 
        catch (IOException ex) 
        {
            new CodeException("Napodařilo se přidat ukončovací řádek v logu StudentLog.txt", ex);
        }
    }
}
