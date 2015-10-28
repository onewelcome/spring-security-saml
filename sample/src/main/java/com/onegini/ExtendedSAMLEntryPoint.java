package com.onegini;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileOptions;

public class ExtendedSAMLEntryPoint extends SAMLEntryPoint {

  @Override
  protected WebSSOProfileOptions getProfileOptions(final SAMLMessageContext context, final AuthenticationException exception) throws MetadataProviderException {
    final HttpServletRequestAdapter inboundMessageTransport = (HttpServletRequestAdapter) context.getInboundMessageTransport();
    final WebSSOProfileOptions profileOptions = super.getProfileOptions(context, exception);
    if("true".equalsIgnoreCase(inboundMessageTransport.getParameterValue("passive"))){
      profileOptions.setPassive(true);
    }
    return profileOptions;
  }
}
