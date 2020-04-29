package bg.codeacademy.spring.gossiptalks.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotHTMLValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotHTML
{
  String message() default "The text contains HTML";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}