package bg.codeacademy.spring.gossiptalks.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotHTMLValidation implements ConstraintValidator<NotHTML, String>
{
  @Override
  public void initialize(NotHTML constraintAnnotation)
  {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext)
  {
    if (value == null || value.isEmpty()) {
      return false;
    }
    else {
      return !DetectHTML.isHtml(value);
    }
  }

}
