package isi.nour.microbank.filter;

import isi.nour.microbank.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// "/*" = intercepte toutes les requêtes sans exception
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI()
                .substring(req.getContextPath().length());

        // La page de login est publique — tout le reste nécessite une session active
        boolean isPublic = path.startsWith("/auth/login")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/");

        if (isPublic) {
            // Ressource publique — on laisse passer sans vérification
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Pas de session active — redirection vers le login
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // Session valide — on laisse passer la requête
        chain.doFilter(request, response);
    }
}