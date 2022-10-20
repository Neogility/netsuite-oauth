# netsuite-oauth
Small Application to Demonstrate the OIDC/OAuth2 flow with Netsuite as the OIDC provider 

# Instructions for Setup
1. Configure OIDC Provider in Netsuite: https://docs.oracle.com/en/cloud/saas/netsuite/ns-online-help/section_160855172780.html
2. Clone this repository
3. Update the src/main/resources/application.properties with the `account-id`, `client-id`, and `client-secret` from Netsuite
4. run the application: `./mvnw clean spring-boot:run`
5. Open a browser to https://localhost:8443
6. Click "Login with Netsuite: click here"
7. Login to Netsuite with your credentials
8. If needed, select the role configured in the OIDC setup in step 1.
9. Click "Continue"

