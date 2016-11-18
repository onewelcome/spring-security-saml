package com.onegini;

import static com.onegini.SAMLRequestConfigurationParameter.ASSERTION_CONSUMER_SERVICE;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_AUTHN_CONTEXTS;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_ENCRYPTION_PARAMETER;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_IDP_TYPE;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_PASSIVE;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_PASSWORD;
import static com.onegini.SAMLRequestConfigurationParameter.PARAM_USERNAME;
import static com.onegini.model.InlineLogin.INLINE_LOGIN_AUTHN_CTX;

import java.util.List;

import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileOptions;

import com.onegini.model.InlineLogin;

public class ExtendedSAMLEntryPoint extends SAMLEntryPoint {

  @Autowired
  @Qualifier("customProfileOptions")
  private CustomWebSSOProfileOptions customWebSSOProfileOptions;

  @Override
  protected WebSSOProfileOptions getProfileOptions(final SAMLMessageContext context, final AuthenticationException exception) throws MetadataProviderException {

    final HttpServletRequestAdapter inboundMessageTransport = (HttpServletRequestAdapter) context.getInboundMessageTransport();
    final WebSSOProfileOptions profileOptions = super.getProfileOptions(context, exception);
    setAssertionConsumerServiceIfPresent(inboundMessageTransport, profileOptions, context);
    setPassiveIfPresentInRequest(inboundMessageTransport, profileOptions);
    setAuthContextIfPresentInRequest(inboundMessageTransport, profileOptions);
    setInlineLoginIfPresentInRequest(inboundMessageTransport, profileOptions);
    return profileOptions;
  }

  private void setAssertionConsumerServiceIfPresent(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions,
                                                    final SAMLMessageContext context) {
    final SPSSODescriptor localEntityRoleMetadata = (SPSSODescriptor) context.getLocalEntityRoleMetadata();
    if (inboundMessageTransport.getParameterValue(ASSERTION_CONSUMER_SERVICE) != null) {
      if (inboundMessageTransport.getParameterValue(ASSERTION_CONSUMER_SERVICE).equals("undefined")) {
        customWebSSOProfileOptions.setAssertionConsumerServiceDefined(false);
      } else {
        for (final AssertionConsumerService assertionConsumerService : localEntityRoleMetadata.getAssertionConsumerServices()) {
          if (assertionConsumerService.getBinding().equals(inboundMessageTransport.getParameterValue(ASSERTION_CONSUMER_SERVICE))) {
            profileOptions.setAssertionConsumerIndex(assertionConsumerService.getIndex());
            return;
          }
        }
      }
    }
  }

  private void setAuthContextIfPresentInRequest(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions) {
    final List<String> authnContexts = inboundMessageTransport.getParameterValues(PARAM_AUTHN_CONTEXTS);
    if (!authnContexts.isEmpty()) {
      profileOptions.setAuthnContexts(authnContexts);
    }
  }

  private void setPassiveIfPresentInRequest(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions) {
    if (Boolean.parseBoolean(inboundMessageTransport.getParameterValue(PARAM_PASSIVE))) {
      profileOptions.setPassive(true);
    }
  }

  private void setInlineLoginIfPresentInRequest(final HttpServletRequestAdapter inboundMessageTransport, final WebSSOProfileOptions profileOptions) {
    final String idpType = inboundMessageTransport.getParameterValue(PARAM_IDP_TYPE);
    final String username = inboundMessageTransport.getParameterValue(PARAM_USERNAME);
    final String password = inboundMessageTransport.getParameterValue(PARAM_PASSWORD);
    final String encryptionParameter = inboundMessageTransport.getParameterValue(PARAM_ENCRYPTION_PARAMETER);

    if (idpType != null && username != null && password != null && encryptionParameter != null &&
        profileOptions.getAuthnContexts() != null && profileOptions.getAuthnContexts().contains(INLINE_LOGIN_AUTHN_CTX)) {
      customWebSSOProfileOptions.setInlineLogin(new InlineLogin(idpType, username, password, encryptionParameter));
    } else {
      customWebSSOProfileOptions.setInlineLogin(null);
    }
  }

}
