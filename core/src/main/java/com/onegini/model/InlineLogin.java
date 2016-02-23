package com.onegini.model;

public class InlineLogin {

  public static final String INLINE_LOGIN_AUTHN_CTX = "urn:onegini:names:SAML:2.0:ac:classes:InlineLogin";

  private final String idpType;
  private final String username;
  private final String password;
  private final String encryptionParameter;

  public InlineLogin(final String idpType, final String username, final String password, final String encryptionParameter) {
    this.idpType = idpType;
    this.username = username;
    this.password = password;
    this.encryptionParameter = encryptionParameter;
  }

  public String getIdpType() {
    return idpType;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEncryptionParameter() {
    return encryptionParameter;
  }

}
