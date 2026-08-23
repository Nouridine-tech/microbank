package isi.nour.microbank.controller;

import isi.nour.microbank.dao.UserDAO;
import isi.nour.microbank.model.User;
import isi.nour.microbank.utils.Tools;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void init() throws ServletException {
        // Crée le compte admin par défaut au premier démarrage s'il n'existe pas encore
        if (userDAO.findByLogin("admin") == null) {
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(Tools.hashPassword("admin123"));
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setRole("ADMIN");
            admin.setActif(true);
            userDAO.save(admin);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/login")) {
            // Affiche le formulaire de connexion
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                    .forward(req, resp);

        } else if (pathInfo.equals("/logout")) {
            // Invalide la session et redirige vers le login
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/login")) {
            login(req, resp);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login    = req.getParameter("login");
        String password = req.getParameter("password");

        // On hache le mot de passe saisi pour le comparer à celui stocké en base
        String hashedPassword = Tools.hashPassword(password);

        User user = userDAO.findByLogin(login);

        if (user == null || !user.getPassword().equals(hashedPassword)) {
            // Identifiants incorrects — on réaffiche le formulaire avec un message d'erreur
            req.setAttribute("error", "Identifiants incorrects.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                    .forward(req, resp);
            return;
        }

        if (!user.isActif()) {
            // Compte désactivé par l'administrateur
            req.setAttribute("error", "Votre compte est désactivé.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                    .forward(req, resp);
            return;
        }

        // Connexion réussie — on stocke l'utilisateur dans la session HTTP
        HttpSession session = req.getSession();
        session.setAttribute("user", user);

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}