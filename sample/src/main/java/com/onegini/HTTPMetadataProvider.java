package com.onegini;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPMetadataProvider extends org.opensaml.saml2.metadata.provider.HTTPMetadataProvider {

  private final Logger log = LoggerFactory.getLogger(HTTPMetadataProvider.class);
  private String pingUrl;

  public HTTPMetadataProvider(final String metadataURL, final int requestTimeout, final String pingUrl) throws MetadataProviderException {
    super(metadataURL, requestTimeout);
    this.pingUrl = pingUrl;
  }

  @Override
  protected byte[] fetchMetadata() throws MetadataProviderException {
    log.info("Trying to fetch metadata for " + getMetadataURI());
    if (pingableHost()) {
      return super.fetchMetadata();
    }
    throw new MetadataProviderException("Unable to fetch metadata from " + getMetadataURI());
  }

  public boolean pingableHost() {
    log.info("Pinging metadata host {}", pingUrl);
    final HttpMethod getMethod = new GetMethod(pingUrl);
    try {
      final int pingHttpResult = createHttpClient().executeMethod(getMethod);
      log.info("Pinging metadata host result: {}", pingHttpResult);
      return pingHttpResult == HttpStatus.SC_OK;
    } catch (IOException e) {
      log.info("Host not pingable {}", getMetadataURI());
      return false;
    }
  }

  private HttpClient createHttpClient() {
    HttpClient httpClient = new HttpClient();
    final HttpClientParams params = new HttpClientParams();
    params.setSoTimeout(getRequestTimeout());
    httpClient.setParams(params);
    httpClient.setTimeout(getRequestTimeout());

    return httpClient;
  }

}
