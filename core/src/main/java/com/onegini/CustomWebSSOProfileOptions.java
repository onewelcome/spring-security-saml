package com.onegini;

import com.onegini.sdk.saml.inlinelogin.InlineLogin;

public class CustomWebSSOProfileOptions {

  private InlineLogin inlineLogin;

  public InlineLogin getInlineLogin() {
    return inlineLogin;
  }

  public void setInlineLogin(final InlineLogin inlineLogin) {
    this.inlineLogin = inlineLogin;
  }
}
