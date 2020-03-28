package com.phantom.tests.filters;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FioAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!request.getMethod()
                .equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);
        return this.getAuthenticationManager()
                .authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String surname = request.getParameter("surname");
        String lastname = request.getParameter("lastname");
        String patronymic = request.getParameter("patronymic");
        String password = obtainPassword(request);

        if (surname == null) {
            surname = "";
        }

        if (lastname == null) {
            lastname = "";
        }

        if (patronymic == null) {
            patronymic = "";
        }

        if (password == null) {
            password = "";
        }

        String fio = surname + lastname + patronymic;
        return new UsernamePasswordAuthenticationToken(fio, password);
    }

}
