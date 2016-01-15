package com.onegini;

public class HttpParameter {

  private final String name;
  private final String value;

  public HttpParameter(final String name, final String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

}
