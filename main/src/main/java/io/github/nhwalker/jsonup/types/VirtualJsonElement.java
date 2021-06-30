package io.github.nhwalker.jsonup.types;

public abstract class VirtualJsonElement extends JsonElement {

  public VirtualJsonElement() {
  }

  public abstract JsonElement asResolved();

  @Override
  public final boolean isVirtual() {
    return true;
  }

  @Override
  public final JsonArray asArray() {
    return asResolved().asArray();
  }

  @Override
  public final JsonBoolean asBoolean() {
    return asResolved().asBoolean();
  }

  @Override
  public final JsonNull asNull() {
    return asResolved().asNull();
  }

  @Override
  public final JsonNumber asNumber() {
    return asResolved().asNumber();
  }

  @Override
  public final JsonObject asObject() {
    return asResolved().asObject();
  }

  @Override
  public final JsonString asString() {
    return asResolved().asString();
  }

  @Override
  public final Kind kind() {
    return asResolved().kind();
  }

}