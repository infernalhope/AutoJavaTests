
package testmanager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import logger.TestLogger;
import test.TestHandler;

public class TestProvider
{

    public static void testEquals(Object expResult, Object result, String message)
    {
        if(!expResult.equals(result))
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }      
    }
    
    public static void testEquals(double expResult, double result, double delta, String message)
    {
        if (!(Math.abs(expResult - result) <= delta))
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testEquals(long expResult, long result, String message)
    {
        if(expResult != result)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }

    public static void testArrayEquals(byte[] expResult, byte[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i] == result[i]))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(int[] expResult, int[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i] == result[i]))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(char[] expResult, char[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i] == result[i]))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(Object[] expResult, Object[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i].equals(result[i])))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(long[] expResult, long[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i] == result[i]))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(float[] expResult, float[] result, float delta, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(Math.abs(expResult[i] - result[i]) <= delta))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(double[] expResult, double[] result, double delta, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(Math.abs(expResult[i] - result[i]) <= delta))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testArrayEquals(short[] expResult, short[] result, String message)
    {
        boolean check = false;
        
        if((expResult != null) && (result != null))
        {
            if(expResult.length == result.length)
            {
                for (int i = 0; i < expResult.length; i++) 
                {
                    if(!(expResult[i] == result[i]))
                    {
                        check = true;
                    }
                }
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testCollectionEquals(Collection first, Collection second, String message)
    {
        boolean check = false;
        
        if(first.size() != second.size())
        {
            check = true;
        }
        else
        {
            Iterator iterator = first.iterator();
            
            for(Object o : second)
            {
                if(o.equals(iterator.next()) == false)
                {
                    check = true;
                    break;
                }
            }
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testNotSame(Object unexpected, Object actual, String message)
    {
        if(unexpected == actual)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testSame(Object unexpected, Object actual, String message)
    {
        if(unexpected != actual)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testNotEquals(Object expResult, Object result, String message)
    {
        if(expResult.equals(result))
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
  
    public static void testNotEquals(double expResult, double result, double delta, String message)
    {
        if (!(Math.abs(expResult - result) <= delta))
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testNotEquals(long expResult, long result, String message)
    {
        if(expResult == result)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testCollectionNotEquals(Collection first, Collection second, String message)
    {
        boolean check = false;
        
        if(first.size() == second.size())
        {
            check = true;
        }
        else
        {
            Iterator iterator = first.iterator();
            
            for(Object o : second)
            {
                if(o.equals(iterator.next()) == true)
                {
                    check = true;
                    break;
                }
            }
        }
        
        if(check == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testGreaterThan(Object smaller, Object greather, String message)
    {
        boolean check = false;
        
        if(!smaller.equals(greather))
        {
            Object[] objects = new Object[]{smaller, greather};
            Arrays.sort(objects);
            
            if(objects[0].equals(greather))
            {
                check = true;
            }
        }
        else
        {
            check = true;
        }
        
        if(check)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testGreaterThan(long smaller, long greather, String message)
    {
        if(greather < smaller)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void testGreaterThan(double smaller, double greather, String message)
    {
        if(greather < smaller)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }    
                
    public static void fail(String message)
    {
        try
        {
            throw new Exception();
        }
        catch(Exception ex)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
            TestHandler.setSkipAfterFatalErrorLog(true);
            throw new Error();
        }
        
    }
    
    public static void isNull(Object object, String message)
    {
        if(object == null)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void isFalse(boolean value, String message)
    {
        if(value == true)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }
    
    public static void isTrue(boolean value, String message)
    {
        if(value == false)
        {
            TestLogger.setResult("Test fail");
            TestLogger.addResultMessage(message);
        }
    }  
}
