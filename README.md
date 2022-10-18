# netsuite-oauth
Small Application to Demonstrate the OIDC/OAuth2 flow with Netsuite as the OIDC provider 

#Instructions
1. Configure OIDC Provider in Netsuite: https://docs.oracle.com/en/cloud/saas/netsuite/ns-online-help/section_160855172780.html
2. Clone this repository
3. Update the src/main/resources/application.properties with the `client-id`, `client-secret` and `account-id` from Netsuite
4. run `./mvnw clean spring-boot:run`
5. Open a browser to https://localhost:8443
