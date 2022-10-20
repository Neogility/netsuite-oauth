package com.neogility.suitetalk.oauth2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    	Map<String, Object> userDetails = new HashMap<>();
    	userDetails.put("email", principal.getAttribute("email"));
    	userDetails.put("company_id", principal.getAttribute("company_id"));
    	userDetails.put("company_name", principal.getAttribute("company_name"));
    	userDetails.put("role_id", principal.getAttribute("role_id"));
    	userDetails.put("role_name", principal.getAttribute("role_name"));
    	userDetails.put("entity_id", principal.getAttribute("entity_id"));
    	userDetails.put("sub", principal.getAttribute("sub"));
        return userDetails;
    }

}