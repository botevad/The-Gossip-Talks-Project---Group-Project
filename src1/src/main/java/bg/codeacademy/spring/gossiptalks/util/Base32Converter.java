package bg.codeacademy.spring.gossiptalks.util;

public class Base32Converter
{
  public static String base32(Integer id)
  {
    String str = Integer.toString(id, 32);
    return str;
  }
}
