package com.onegini;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestParametersReader {

  private final HttpServletRequest request;

  public HttpRequestParametersReader(final HttpServletRequest request) {
    this.request = request;
  }

  @SuppressWarnings("unchecked")
  public List<HttpParameter> getParameters() {
    final Map<String, String[]> requestParameters = request.getParameterMap();
    final List<HttpParameter> forwardedParameters = new ArrayList<HttpParameter>();

    for (final Map.Entry<String, String[]> requestParameter : requestParameters.entrySet()) {
      addParameters(requestParameter, forwardedParameters);
    }

    return forwardedParameters;
  }

  private void addParameters(final Map.Entry<String, String[]> requestParameter, final List<HttpParameter> forwardedParameters) {
    for (final String value : requestParameter.getValue()) {
      forwardedParameters.add(new HttpParameter(requestParameter.getKey(), value));
    }
  }

}
