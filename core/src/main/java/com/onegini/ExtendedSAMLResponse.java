package com.onegini;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedSAMLResponse extends HttpServletResponseWrapper {

  private final static Logger logger = LoggerFactory.getLogger(ExtendedSAMLResponse.class);

  private final HttpServletRequest request;

  public ExtendedSAMLResponse(final HttpServletRequest request, final HttpServletResponse response) {
    super(response);
    this.request = request;
  }

  @Override
  public void sendRedirect(final String location) throws IOException {
    final List<HttpParameter> forwardedParameters = new HttpRequestCustomParameterReader(request).getParameters();
    final String redirectionUrl = buildRedirectionUrl(location, forwardedParameters);
    super.sendRedirect(redirectionUrl);
  }

  private String buildRedirectionUrl(final String originalLocation, final List<HttpParameter> forwardedParameters) {
    try {
      final URIBuilder uriBuilder = new URIBuilder(originalLocation);
      for (final HttpParameter param : forwardedParameters) {
        uriBuilder.addParameter(param.getName(), param.getValue());
      }
      return uriBuilder.build().toString();
    } catch (final URISyntaxException e) {
      logger.info("Unable to build url.");
      return originalLocation;
    }
  }
}
