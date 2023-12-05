package com.example.keycloakdemo.converter;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Value("${jwt.auth.converter.principal-name}")
    private String principalName;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;
    private final String ROLES = "roles";
    private final String RESOURCE_ACCESS = "resource_access";
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities =
                Stream.concat(Objects.requireNonNull(new JwtGrantedAuthoritiesConverter().convert(jwt)).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt,
                grantedAuthorities,
                getPrincipalName(jwt));
    }

    private String getPrincipalName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if(jwt.getClaim(principalName) != null) {
            claimName = jwt.getClaim(principalName);
        }
        return claimName;
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        if(jwt.getClaim("resource_access") == null || resourceId == null) {
            return Set.of();
        }
        var resourceAccess = new HashMap<>(jwt.getClaim(RESOURCE_ACCESS));
        LinkedTreeMap<String, ArrayList<String>> stringArrayListLinkedTreeMap =
                (LinkedTreeMap<String, ArrayList<String>>) resourceAccess.get(resourceId);
        if(stringArrayListLinkedTreeMap.get(ROLES) == null) {
            return Set.of();
        }
        ArrayList<String> roles = stringArrayListLinkedTreeMap.get(ROLES);
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toSet());
    }
}
