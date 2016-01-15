package com.onegini;

import static com.onegini.SAMLRequestConfigurationParameter.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestCustomParameterReader {

  private final static List<String> BLACKLISTED_PARAMETERS = new ArrayList<String>();
  private final HttpServletRequest request;

  static {
    BLACKLISTED_PARAMETERS.add(PARAM_AUTHN_CONTEXTS);
    BLACKLISTED_PARAMETERS.add(PARAM_PASSIVE);
  }

  public HttpRequestCustomParameterReader(final HttpServletRequest request) {
    this.request = request;
  }

  @SuppressWarnings("unchecked")
  public List<HttpParameter> getParameters() {
    final Map<String, String[]> requestParameters = request.getParameterMap();
    final List<HttpParameter> forwardedParameters = new ArrayList<HttpParameter>();

    for (final Map.Entry<String, String[]> requestParameter : requestParameters.entrySet()) {
      if (isOnWhitelist(requestParameter.getKey())) {
        addParameters(requestParameter, forwardedParameters);
      }
    }

    return forwardedParameters;
  }

  private void addParameters(final Map.Entry<String, String[]> requestParameter, final List<HttpParameter> forwardedParameters) {
    for (final String value : requestParameter.getValue()) {
      forwardedParameters.add(new HttpParameter(requestParameter.getKey(), value));
    }
  }

  private boolean isOnWhitelist(final String parameterName) {
    return !BLACKLISTED_PARAMETERS.contains(parameterName);
  }

}
