Spring SAML
====================

Onegini uses this library to test the SAML integration of the Onegini platform

Spring SAML Extension allows seamless inclusion of SAML 2.0 Service Provider capabilities in Spring applications. All products supporting SAML 2.0 in Identity Provider mode (e.g. ADFS 2.0, Shibboleth, OpenAM/OpenSSO, Ping Federate, Okta) can be used to connect with Spring SAML Extension.

Web:
http://projects.spring.io/spring-security-saml/

Sources: https://github.com/spring-projects/spring-security-saml
Documentation: http://docs.spring.io/spring-security-saml/docs/1.0.x-SNAPSHOT/reference/
Jira: https://jira.spring.io/browse/SES/component/10711/
CI: https://build.springsource.org/browse/SES

Releases:
Final: http://repo.spring.io/list/release/org/springframework/security/extensions/
Milestone: http://repo.spring.io/list/milestone/org/springframework/security/extensions/
Snapshot: http://repo.spring.io/list/snapshot/org/springframework/security/extensions/

Support:
Stackoverflow: http://stackoverflow.com/questions/tagged/spring-saml
Forum: http://forum.spring.io/forum/spring-projects/security/saml
Commercial: vladimir@v7security.com

Online demo: http://saml-federation.appspot.com/


IdP Metadata Configuration
==========================

The Sample module needs a running SAML Identity Provider (IdP) for the authentication. The default configuration uses the SAML metadata for a Onegini IdP that 
runs on http://itest.onegini.me:8989. 

The IdP metadata for http://dev.onegini.me:8989 (local development) is loaded by running the sample with the `local` profile:

    mvn tomcat7:run -P local

The IdP metadata for http://dev.onegini.me:8990 (docker) is loaded by running the sample with the `docker` profile:

    mvn tomcat7:run -P docker

Metadata for each of the idp instance is loaded via http (IDP_METADATA_URL property).

Requests preparation
====================

Passive request
---------------

Passive request param: `passive=true`

Example: http://localhost:8080/spring-security-saml2-sample/?passive=true

Cookie based authentication
---------------------------

Passive request cookie based request params: `passive=true&authnContexts=urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession`

Example: http://localhost:8080/spring-security-saml2-sample/?passive=true&authnContexts=urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession

The `authnContexts` param can be used for other valid authentication context values too.

Inline login
------------

Inline login request params: 

 * `authnContexts=urn:onegini:names:SAML:2.0:ac:classes:InlineLogin`
 * `idpType=unp_idp`
 * `username=<USERNAME>`
 * `password=<PASSWORD_BASE_64>`
 * `encryptionParameter=<ENCRYPTION_PARAMETER_BASE_64>`

Below is example with encryption using development password encryption key.

Example inline login with passive: http://localhost:8080/spring-security-saml2-sample/?authnContexts=urn:onegini:names:SAML:2.0:ac:classes:InlineLogin&idpType=unp_idp&username=cim-352@x.pl&password=o%2FMCR6uS%2FRAmOse1%2B3ngU6gjf%2F%2Br8h4xWw%3D%3D&encryptionParameter=BVLdWx%2F%2FevkFUt1bH%2F96%2BQ%3D%3D&passive=true

Optional authentication
-----------------------

Optional authentication request params

* `authnContexts=urn:oasis:names:tc:SAML:2.0:ac:classes:Password&authnContexts=urn:com:onegini:saml:OptionalAuthentication` 
* `authnContexts=urn:oasis:names:tc:SAML:2.0:ac:classes:Password&authnContexts=urn:com:onegini:saml:OptionalAuthentication&authnContexts=urn:com:onegini:saml:NoRegistration`
 
Example:
 `http://localhost:8080/spring-security-saml2-sample/?authnContexts=urn:oasis:names:tc:SAML:2.0:ac:classes:Password&authnContexts=urn:com:onegini:saml:OptionalAuthentication&authnContexts=urn:com:onegini:saml:NoRegistration`


Artifact binding
-----------------------

To be able to send requests with artifact binding, SP needs to support assertion consumer service, it can be checked in SP metadata, eg.
```
<?xml version="1.0" encoding="UTF-8"?>
<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" ID="spring_security_saml" entityID="spring:security:saml">
   <md:SPSSODescriptor AuthnRequestsSigned="true" WantAssertionsSigned="true" protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol">
      <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="http://localhost:8080/spring-security-saml2-sample/saml/SSO" index="0" isDefault="true" />
      <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="http://localhost:8080/spring-security-saml2-sample/saml/SSO" index="1" />
   </md:SPSSODescriptor>
</md:EntityDescriptor>

```

With above metadata, `HTTP-POST` binding will be used by default. To use different assertion consumer service, please pass `assertionBinding` parameter to the url with appropriate protocol binding, eg.:

`http://localhost:8080/spring-security-saml2-sample?assertionBinding=urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact`