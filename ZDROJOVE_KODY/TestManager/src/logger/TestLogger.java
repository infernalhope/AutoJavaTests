
package logger;

import codeHandlers.ZipHandler;
import exceptions.CodeException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import test.TestHandler;



public class TestLogger
{

    private static boolean newStudentTest = true;
    private static String studentName;
    private static String result = "OK";
    private static StringBuilder resultInfo = new StringBuilder();
    private static String score = "OK";
    
    public static void logResult() 
    {
        String csvName = "result.csv";
        String destination = ZipHandler.getTestFolder();
        File directory = new File(destination);
        File csvFile = new File(destination +File.separator +csvName);
        
        checkCSVFile(directory, csvName);

        try
        {
            String message = generateInput();
            FileOutputStream fos = new FileOutputStream(csvFile, true);
            OutputStreamWriter csvWriter = new OutputStreamWriter(fos, "cp1250");
            
            csvWriter.write(message, 0, message.length());
            csvWriter.write(System.lineSeparator(), 0, System.lineSeparator().length());
            
            csvWriter.close();
            fos.close();    
        } catch (FileNotFoundException ex) 
        {
            new CodeException("CSV soubor \""  +csvFile +"\" neexistuje nebo není dostupný v době zápisu výsledků testů.", ex);
        } 
        catch (IOException ex) 
        {
            new CodeException("Nastala chyba při zápisu výsledků testů do CSV souboru: " +csvFile, ex);
        }
        
    }

    private static void checkCSVFile(File directory, String csvName) 
    {
        CSVFileFilter CSVff = new CSVFileFilter();
        
        File[] files = directory.listFiles(CSVff);
        
        if(files.length == 0)
        {
            createCSVFile(csvName, directory.toString());
        }
    }

    private static void createCSVFile(String csvName, String directory) 
    {
        Path csvFile = Paths.get(directory +File.separator +csvName);
        
        try 
        {
            Files.createFile(csvFile);
        } 
        catch (IOException ex) 
        {
            new CodeException("Nepodařilo se vytvořit CSV soubor", ex);
        }
    }

    private static String generateInput() 
    {
        String user = ZipHandler.getUser();
        String testMethodName = TestHandler.getTestMethodName();
        String line = "";
        String result = getResult();
        String message = "";
        
        if(!getResultInfo().isEmpty())
        {
            message = "\"" +getResultInfo() +"\"";
        }    
        
        
        if(user.equals(getStudentName()) == false)
        {
            setStudentName(user);
            line = user +"," +testMethodName +"," +result +"," +message; 
        }
        else
        {
            line = user +"," +testMethodName +"," +result +"," +message; 
        }
        
        return line;
    }

    public static String getStudentName() 
    {
        return studentName;
    }

    public static void setStudentName(String studentName) 
    {
        TestLogger.studentName = studentName;
    }
    
    public static void printCSVHeader()
    {
        String csvName = "result.csv";
        String destination = ZipHandler.getTestFolder();
        File directory = new File(destination);
        File csvFile = new File(destination +File.separator +csvName);
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy, HH:mm:ss");
        String date = format.format(c.getTime());
        String toErrorMessageEncoding = "none";
        
        checkCSVFile(directory, csvName);
        
        try
        {
            FileOutputStream fos = new FileOutputStream(csvFile, false);
            OutputStreamWriter csvWriter = new OutputStreamWriter(fos, "cp1250");
            String line = "<student>,<test-metoda>,<výsledek>,<poznámky>,\""+date+"\"";
            toErrorMessageEncoding = csvWriter.getEncoding();
            
            csvWriter.write(line, 0, line.length());
            csvWriter.write(System.lineSeparator());
            
            csvWriter.close();
            fos.close();
        } catch (FileNotFoundException ex) 
        {
            new CodeException("CSV soubor \""  +csvFile +"\" neexistuje nebo není dostupný v době zápisu hlavičky testování.", ex);
        } 
        catch (UnsupportedEncodingException ex) 
        {
            new CodeException("Nastal problém se specifikací kódové tabulky pro zápis do CSV souboru. Zvolené kódování: " +toErrorMessageEncoding, ex);
        } 
        catch (IOException ex) 
        {
            new CodeException("Nastala chyba při zápisu hlavičky testování do CSV souboru: " +csvFile, ex);
        } 
    }

    public static String getResult() 
    {
        return result;
    }

    public static void setResult(String result) 
    {
        String temp = TestLogger.result;
        
        if(!temp.equals("Fatal error"))
        {
            if(!temp.equals("Test error"))
            {
                if(!temp.equals("Test fail"))
                {
                    if(!result.equals("OK"))
                    {
                        TestLogger.result = result;
                        setScore("FAIL"); 
                    }
                }
                else if(result.equals("Test error"))
                {
                    TestLogger.result = result;
                    setScore("FAIL"); 
                }
            }
            else if(result.equals("Fatal error"))
            {
                TestLogger.result = result;
                setScore("FAIL"); 
            }
        }
    }
    
    public static void resetResult() 
    {
        TestLogger.result = "OK";
    }

    public static String getResultInfo()
    {
        String ret = resultInfo.toString();
        if(!ret.isEmpty())
        {
            ret = ret.substring(0, ret.lastIndexOf(";"));
        }
        
        return ret;
    }

    public static void addResultMessage(String resultInfo) 
    {
        TestLogger.resultInfo.append(resultInfo +"; ");
    }
    
    public static void resetResultInfo() 
    {
        TestLogger.resultInfo = new StringBuilder();
    }
    
    private static boolean APPEND_TO_GENERAL_LOG = false;
    public static void logGeneralResult() 
    {
        String csvName = "general-result.csv";
        String destination = ZipHandler.getTestFolder();
        File directory = new File(destination);
        File csvFile = new File(destination +File.separator +csvName);
        
        checkCSVFile(directory, csvName);

        try
        {
            String message = ZipHandler.getUser() +"," +getScore();
            FileOutputStream fos = new FileOutputStream(csvFile, APPEND_TO_GENERAL_LOG);
            OutputStreamWriter csvWriter = new OutputStreamWriter(fos, "cp1250");
            
            csvWriter.write(message, 0, message.length());
            csvWriter.write(System.lineSeparator(), 0, System.lineSeparator().length());
            
            csvWriter.close();
            fos.close();
            APPEND_TO_GENERAL_LOG = true;
        } 
        catch (FileNotFoundException ex) 
        {
            new CodeException("CSV soubor \""  +csvFile +"\" neexistuje nebo není dostupný v době zápisu výsledků testů.", ex);
        } 
        catch (IOException ex) 
        {
            new CodeException("Nastala chyba při zápisu výsledků testů do CSV souboru: " +csvFile, ex);
        }
        
    }

    private static String getScore() 
    {
        return score;
    }

    private static void setScore(String score) 
    {
        TestLogger.score = score;
    }
    
    public static void resetScore()
    {
        setScore("OK");
    }
    
    
}
