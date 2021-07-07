package example;

import java.lang.reflect.Type;
import java.util.Map;

import io.github.nhwalker.jsonup.adapters.common.JavaTimeConverterSet;
import io.github.nhwalker.jsonup.ann.AsJson;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FullJsonConverter;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonObject;

@AsJson(Person.AsJson.class)
@AsJson(JavaTimeConverterSet.class)
public class Person {
  
  public Person(String firstName, String lastName) {

  }

  public String firstName() {
    return null;
  }

  public String lastName() {
    return null;
  }

  public enum AsJson implements FullJsonConverter<Person> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Person.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Person value) {
      return new JsonObject(o -> {
        o.add("firstName", value.firstName());
      });
    }

    @Override
    public Person fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      Map<String, JsonElement> map = json.asObject().asMap();
      String firstName = map.get("firstName").asStringLenient().stringValue();
      String lastName = map.get("lastName").asStringLenient().stringValue();
      return new Person(firstName, lastName);
    }

  }
}
