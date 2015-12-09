package com.onegini;

import java.util.List;

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
    setPassiveIfPresentInRequest(inboundMessageTransport, profileOptions);
    setAuthContextIfPresentInRequest(inboundMessageTransport, profileOptions);
    return profileOptions;
  }

  private void setAuthContextIfPresentInRequest(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions) {
    final List<String> authnContexts = inboundMessageTransport.getParameterValues("authnContexts");
    if(!authnContexts.isEmpty()){
      profileOptions.setAuthnContexts(authnContexts);
    }
  }

  private void setPassiveIfPresentInRequest(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions) {
    if ("true".equalsIgnoreCase(inboundMessageTransport.getParameterValue("passive"))) {
      profileOptions.setPassive(true);
    }
  }
}
