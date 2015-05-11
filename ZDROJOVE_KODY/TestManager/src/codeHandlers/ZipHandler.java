
package codeHandlers;

import exceptions.CodeException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import logger.CodeLogger;
import logger.TestLogger;
import test.TestHandler;


public class ZipHandler 
{
    
    private static String userName = "";
    private static String testFolder = "";
    private static String tempFolder = System.getProperty("java.io.tmpdir") + "\\AutomaticTest\\";
    
    
    /**
     * Trida se bude spoustet pres prikazovy radek a bude ocekavat 2 argumenty
     * args[0] = Cesta k .zip souboru od ucitele (zadani testu)
     * args[1] = Cesta k .zip souboru s ukoly studentu
     * @param args = args[0], cesta k ucitelskemu zipu.
     *               args[1], cesta k zipu s ukoly.
    */
    public static void main(String[] args)
    {
        //pokud existuje slozka AutomaticTest, smazu ji
        checkTempFolder();

        
        //unzipnu uciteluv .zip a ziskam adresy .jar souboru ucitele
        File teacherZipFile = new File(args[0]);
        String teacherFoldername = separateFolderName(teacherZipFile.toString());
        List<String> teacherJarFiles = unZipIt(teacherZipFile.toString(), teacherFoldername);
        
        //nastaveni cesty do slozky, kde se ulozi CSV soubor (stejna jako slozka se .zip ukoly)
        setTestFolder(args[1]);
        
        //pripravim si CSV soubor (vytvorit/otevrit a napsat hlavicku)
        TestLogger.printCSVHeader();
        
        //vratim si nazvy .jar souboru a z nich vyseparuji "[jmeno] [prijmeni]"
        Set<String> studentDirectoryNames = new TreeSet<>();
        for(String jarFile : getJarFiles(args[1]))
        {
            studentDirectoryNames.add(separateJarName(jarFile));
        }
        
        //unzipnu soubor a ulozim si vsechny adresy .jar souboru
        List<String> jarList = unZipItAll(args[1], studentDirectoryNames);
        
        
        //pro kazdeho studenta ...
        for(String jarFileName : studentDirectoryNames)
        {
            try
            {
                setUser(jarFileName);
                List<String> studentJarFilesList = new ArrayList<>();
                
                //vyseparuji adresy .jar souboru aktualniho studenta
                for(String jarPath : jarList)
                {
                    if(jarPath.contains(jarFileName))
                    {
                        studentJarFilesList.add(jarPath);
                    }
                }
                
                if(studentJarFilesList.isEmpty()) throw new Exception();
                TestHandler.prepareTest(teacherJarFiles, studentJarFilesList);
            }
            catch(Exception ex)
            {
                TestLogger.setResult("Test error");
                TestLogger.addResultMessage("Student neodevzdal .jar");
                TestLogger.logResult();
            }
            finally
            {
                TestLogger.logGeneralResult();
                TestLogger.resetScore();
            }
            
        }
        
        addErrorLogEndLine();
    }
   
    private static void destroyDestFolder(String destination)
    {  
        File folder = new File(destination);
        
        File[] files = folder.listFiles();
        
        try 
        {
            if(files.length != 0)
            {
                for (int i = 0; i < files.length; i++) 
                {
                    if(files[i].isDirectory())
                    {
                        destroyDestFolder(files[i].toString());
                    }
                    else
                    {
                        Files.delete(files[i].toPath());
                    }

                }
            } 
        
            Files.delete(folder.toPath());
        } 
        catch (IOException ex) 
        {
            new CodeException("Došlo k chybě při mazání souboru: " +folder.getName(), ex);
        }
    }

    public static String getUser() 
    {
        return userName;
    }

    public static void setUser(String user) 
    {
        userName = user;
    }

    public static String getTestFolder() 
    {
        return testFolder;
    }

    public static void setTestFolder(String testFolder) 
    {
        int index = testFolder.lastIndexOf("\\");
        
        if(index != -1)
        {
            ZipHandler.testFolder = testFolder.substring(0, index);
        }
        else
        {
            ZipHandler.testFolder = ".\\";
        }
        
        
    }

    private static void checkTempFolder() 
    {
        File tempFolder = new File(ZipHandler.tempFolder);
        
        if(tempFolder.exists())
        {
            destroyDestFolder(ZipHandler.tempFolder);
        }
    }

    private static void addErrorLogEndLine() 
    {
        if(CodeLogger.isLogAppended())
        {
            CodeLogger.addEndLine();
        }
    }

    private static String separateFolderName(String path)
    {
        String ret = path;
        boolean stop = false;
        int index = 0;
        
        while(!stop)
        {
            index = path.indexOf("\\");
            
            if(index != -1)
            {
                path = path.substring(index+1);
            }
            else
            {
                path = path.substring(0,path.length()-4)+"\\";
                ret = path;
                stop = true;
            }
        
        }

        return ret;
    }
    
    private static List<String> unZipIt(String zipFile, String outputFolder)
    {
        List<String> ret = new ArrayList<>();
        String currentEntry = "none";
        
        try 
        {
            int BUFFER = 2048;
            File file = new File(zipFile);
            
            ZipFile zip = new ZipFile(file, Charset.forName("CP852"));
            String newPath = tempFolder + File.separatorChar + outputFolder;
            
            new File(newPath).mkdir();
            Enumeration zipFileEntries = zip.entries();
            
            while (zipFileEntries.hasMoreElements())
            {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                currentEntry = entry.getName();
                File destFile = new File(newPath, currentEntry);
                
                if(entry.getName().contains("TestManager")) continue;
                if(destFile.toString().contains(".jar"))
                {
                    ret.add(destFile.toString());
                }
                
                File destinationParent = destFile.getParentFile();
                
                destinationParent.mkdirs();
                
                if (!entry.isDirectory())
                {
                    BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,BUFFER);
                    
                    while ((currentByte = bis.read(data, 0, BUFFER)) != -1) 
                    {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    bis.close();
                }
            }       
        } 
        catch (IOException ex) 
        {
            System.out.println("Chyba při otevírání nebo zápisu do zip souboru: " 
                    +tempFolder +File.separatorChar +outputFolder +File.separatorChar +currentEntry);
            new CodeException("Chyba při otevírání nebo zápisu do zip souboru: " 
                    +tempFolder +File.separatorChar +outputFolder +File.separatorChar +currentEntry, ex);
        }
        
        return ret;
    }
    
    private static List<String> unZipItAll(String zipFile, Set<String> folders)
    {
        List<String> ret = new ArrayList<>();
        int index = 0;
        List<String> outputFolder = new ArrayList<>();
        outputFolder.addAll(folders);
        String currentEntry = "none";
        String studentFolder = "none";
        
        try 
        {
            int BUFFER = 2048;
            File file = new File(zipFile);
            ZipFile zip = new ZipFile(file, Charset.forName("CP852"));
            Enumeration zipFileEntries = zip.entries();
            File destFile = new File("");
            
            while (zipFileEntries.hasMoreElements())
            {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                currentEntry = entry.getName();

                if(entry.getName().contains("TestManager")) continue;

                if(entry.getName().contains(".jar") || entry.getName().contains(".zip"))
                {
                    if(currentEntry.contains(studentFolder) == false)
                    {
                        index++;
                        studentFolder = separateJarName(currentEntry);
                    }

                    String newPath = tempFolder + File.separatorChar + studentFolder;
                    File newFolder = new File(newPath);
                    newFolder.mkdir();
                    destFile = new File(newPath, currentEntry);   
                    File destinationParent = destFile.getParentFile();
                    destinationParent.mkdirs();

                    if (!entry.isDirectory())
                    {
                        BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];
                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos,BUFFER);

                        while ((currentByte = bis.read(data, 0, BUFFER)) != -1) 
                        {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        fos.close();
                        bis.close();

                        if(entry.getName().contains(".jar"))
                        {
                            File newName = new File(tempFolder + File.separatorChar
                                + studentFolder + File.separatorChar + studentFolder +".jar");
                            destFile.renameTo(newName);
                            ret.add(newName.toString());
                        }
                        else
                        {
                            index--;
                        }



                        if(currentEntry.endsWith(".zip"))
                        {
                            ret.addAll(index, unZipIt(destFile.toString(), studentFolder));
                        }
                    } 
                }
            }       
        } 
        catch (IOException ex) 
        {
            new CodeException("Chyba při otevírání nebo zápisu do zip souboru: " 
                    +tempFolder +File.separatorChar +studentFolder +File.separatorChar +currentEntry, ex);
        }
        
        return ret;
    }

    private static List<String> getJarFiles(String studentZipFile) 
    {
        List<String> ret = new ArrayList<>();
        
        ZipFile zip;
        try 
        {
            zip = new ZipFile(studentZipFile, Charset.forName("CP852"));
            Enumeration zipFileEntries = zip.entries();
            
            while (zipFileEntries.hasMoreElements())
            {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                ret.add(entry.getName());
            }
        } 
        catch (IOException ex) 
        {
            new CodeException("Chyba při otevírání zip souboru: " +studentZipFile, ex);
        }
        
        return ret;
    }

    private static String separateJarName(String jarFile) 
    {
        String ret = "";
        int index = jarFile.indexOf("_");
        ret = jarFile.substring(0, index);

        return ret;
    }
    
    
}
