package com.onegini;

import com.onegini.model.InlineLogin;

public class CustomWebSSOProfileOptions {

  private InlineLogin inlineLogin;
  private boolean assertionConsumerServiceDefined = true;

  public boolean isAssertionConsumerServiceDefined() {
    return assertionConsumerServiceDefined;
  }

  public void setAssertionConsumerServiceDefined(final boolean assertionConsumerServiceDefined) {
    this.assertionConsumerServiceDefined = assertionConsumerServiceDefined;
  }

  public InlineLogin getInlineLogin() {
    return inlineLogin;
  }

  public void setInlineLogin(final InlineLogin inlineLogin) {
    this.inlineLogin = inlineLogin;
  }
}
