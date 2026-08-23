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
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Seul l'ADMIN peut accéder à la gestion des utilisateurs
        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listUsers(req, resp);
        } else if (pathInfo.equals("/new")) {
            showForm(req, resp, new User());
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                User user = userDAO.findById(id);
                if (user != null) {
                    showForm(req, resp, user);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            createUser(req, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update":
                        updateUser(req, resp, id);
                        break;
                    case "delete":
                        deleteUser(req, resp, id);
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/users/list.jsp")
                .forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/users/form.jsp")
                .forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = new User();
        extractUser(req, user, true);
        userDAO.save(user);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        // Un admin ne peut pas supprimer son propre compte
        User user = userDAO.findById(id);
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        extractUser(req, user, false);
        userDAO.update(user);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {
        User currentUser = getConnectedUser(req);
        // Un utilisateur ne peut pas supprimer son propre compte
        if (currentUser.getId() == id) {
            resp.sendRedirect(req.getContextPath() + "/users?error=Vous ne pouvez pas supprimer votre propre compte.");
            return;
        }
        userDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private void extractUser(HttpServletRequest req, User user, boolean isNew) {
        user.setNom(req.getParameter("nom"));
        user.setPrenom(req.getParameter("prenom"));
        user.setLogin(req.getParameter("login"));
        user.setRole(req.getParameter("role"));
        user.setActif("on".equals(req.getParameter("actif")));

        // Le mot de passe est obligatoire à la création, optionnel à la modification
        String password = req.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(Tools.hashPassword(password));
        } else if (isNew) {
            user.setPassword(Tools.hashPassword("admin123"));
        }
    }

    // Vérifie si l'utilisateur connecté est ADMIN
    private boolean isAdmin(HttpServletRequest req) {
        User user = getConnectedUser(req);
        return user != null && "ADMIN".equals(user.getRole());
    }

    private User getConnectedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (User) session.getAttribute("user") : null;
    }
}