
package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//RUNTIME znamena, ze muzu anotaci zpracovavat skrze reflexi
//METHOD znamena, ze anotace muze byt pouzita pro metodu

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectInstanceOfClass
{
    
    String classNameRegex() default "";
}


