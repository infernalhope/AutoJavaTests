
package logger;

import java.io.File;
import java.io.FileFilter;

public class LogFileFilter implements FileFilter
{

    @Override
    public boolean accept(File file) 
    {
         return file.exists() && 
                 file.isFile() && 
                 file.getName().contains("ErrorLog.txt");
    }
    
    
}
