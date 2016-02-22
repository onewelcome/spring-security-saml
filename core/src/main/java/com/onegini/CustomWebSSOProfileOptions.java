package com.onegini;

import com.innovation_district.saml.common.model.inlinelogin.InlineLoginDto;

public class CustomWebSSOProfileOptions {

  private InlineLoginDto inlineLogin;

  public InlineLoginDto getInlineLogin() {
    return inlineLogin;
  }

  public void setInlineLogin(final InlineLoginDto inlineLogin) {
    this.inlineLogin = inlineLogin;
  }
}
