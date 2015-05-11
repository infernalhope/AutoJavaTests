
package exceptions;

import logger.CodeLogger;

public class CodeException extends Exception
{
    private static final String newLine = System.getProperty("line.separator");

    public CodeException()
    {
    
    }
    
    public CodeException(String cause, Throwable thrwbl) 
    {
        super(cause, thrwbl);
        
        Throwable t = thrwbl;
        StringBuilder sb = new StringBuilder();
        sb.append(cause);
        sb.append(newLine);
        sb.append(t.getMessage());

        while(t != null)
        {
            sb.append(newLine);
            sb.append("--->");
            sb.append(t.toString());
            for(StackTraceElement ste : t.getStackTrace())
            {
                sb.append(newLine);
                sb.append(ste.toString());
            }
            t = t.getCause();
        }

        sb.append(newLine);
        sb.append("---------------------------");
        logException(sb.toString());
    }

    public CodeException(Throwable thrwbl) 
    {
        super(thrwbl);
        
        Throwable t = thrwbl;
        StringBuilder sb = new StringBuilder();
        sb.append(t.getMessage());

        while(t != null)
        {
            sb.append(newLine);
            sb.append("--->");
            sb.append(t.toString());
            for(StackTraceElement ste : t.getStackTrace())
            {
                sb.append(newLine);
                sb.append(ste.toString());
            }
            t = t.getCause();
        }

        sb.append(newLine);
        sb.append("---------------------------");
        logException(sb.toString());
    }

    @Override
    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();

        for(StackTraceElement ste : this.getStackTrace())
        {
            sb.append(newLine);
            sb.append(ste.toString());
        }
        
        return sb.toString(); 
    }

    public void logException(String message)
    {
        CodeLogger.logException(message);
    }
    
    
}
