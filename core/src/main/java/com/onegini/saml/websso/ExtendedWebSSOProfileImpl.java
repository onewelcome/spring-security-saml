package com.onegini.saml.websso;

import java.util.Optional;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;

import com.onegini.CustomWebSSOProfileOptions;
import com.onegini.sdk.saml.inlinelogin.InlineLogin;
import com.onegini.sdk.saml.inlinelogin.InlineLoginBuilder;
import com.onegini.sdk.saml.inlinelogin.InlineLoginCredentials;
import com.onegini.sdk.saml.inlinelogin.InlineLoginCredentialsBuilder;

public class ExtendedWebSSOProfileImpl extends WebSSOProfileImpl {

  @Autowired
  @Qualifier("customProfileOptions")
  private CustomWebSSOProfileOptions customWebSSOProfileOptions;

  @Override
  protected AuthnRequest getAuthnRequest(final SAMLMessageContext context, final WebSSOProfileOptions options, final AssertionConsumerService assertionConsumer,
                                         final SingleSignOnService bindingService) throws SAMLException, MetadataProviderException {
    final AuthnRequest request = super.getAuthnRequest(context, options, assertionConsumer, bindingService);
    buildExtensions(request, options);

    return request;
  }

  private void buildExtensions(final AuthnRequest request, final WebSSOProfileOptions options) {
    final XMLObject inlineLoginExtension = buildInlineLoginExtension(request, options);
    if (inlineLoginExtension != null) {
      final Extensions extensions = new ExtensionsBuilder().buildObject();
      extensions.getUnknownXMLObjects().add(inlineLoginExtension);
      request.setExtensions(extensions);
    }
  }

  private XMLObject buildInlineLoginExtension(final AuthnRequest request, final WebSSOProfileOptions options) {
    if (customWebSSOProfileOptions.getInlineLogin() != null) {
      final InlineLogin inlineLogin = customWebSSOProfileOptions.getInlineLogin();
      final Optional<InlineLoginCredentials> credentialsDto = customWebSSOProfileOptions.getInlineLogin().getCredentials();
      if (credentialsDto.isPresent()) {
        final XSAny credentials = new InlineLoginCredentialsBuilder().buildObject(
            credentialsDto.get().getUsername(), credentialsDto.get().getPassword(), credentialsDto.get().getEncryptionParameter());

        return new InlineLoginBuilder().buildObject(inlineLogin.getIdpType(), credentials);
      } else {
        return new InlineLoginBuilder().buildObject(inlineLogin.getIdpType());
      }
    } else {
      return null;
    }
  }
}
