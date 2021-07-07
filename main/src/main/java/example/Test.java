package example;

public class Test {
  public static void main(String[] args) {
    System.out.println(String[].class);
    System.out.println(Iterable.class.isAssignableFrom(String[].class));
  }
}
