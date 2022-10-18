# netsuite-oauth
Small Application to Demonstrate the OIDC/OAuth2 flow with Netsuite as the OIDC provider 

#Instructions for Setup
1. Configure OIDC Provider in Netsuite: https://docs.oracle.com/en/cloud/saas/netsuite/ns-online-help/section_160855172780.html
2. Clone this repository
3. Update the src/main/resources/application.properties with the `client-id`, `client-secret` and `account-id` from Netsuite

#Instructions for Netsuite userinfo endpoint
4. run the application: `./mvnw clean spring-boot:run`
5. Open a browser to https://localhost:8443
6. Click "Login with Netsuite: click here"
7. Login to Netsuite with your credentials
8. If needed, select the role configured in the OIDC setup in step 1.
9. Click "Continue"
10. Notice the 400 error from the userinfo endpoint in the logs.

#Instructions for local test userinfo endpoint
4. run the application: `spring_profiles_active=hack ./mvnw clean spring-boot:run`
5. Open a browser to https://localhost:8443
6. Click "Login with Netsuite: click here"
7. Login to Netsuite with your credentials
8. If needed, select the role configured in the OIDC setup in step 1.
9. Click "Continue"
10. Click "Query"
11. Notice the query succeeds