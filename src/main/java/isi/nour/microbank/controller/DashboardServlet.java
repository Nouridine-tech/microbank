package isi.nour.microbank.controller;

import isi.nour.microbank.dao.AccountDAO;
import isi.nour.microbank.dao.ClientDAO;
import isi.nour.microbank.dao.OperationDAO;
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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserDAO      userDAO      = new UserDAO();
    private final ClientDAO    clientDAO    = new ClientDAO();
    private final AccountDAO   accountDAO   = new AccountDAO();
    private final OperationDAO operationDAO = new OperationDAO();

    @Override
    public void init() throws ServletException {
        // Crée le compte admin par défaut au premier démarrage
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

        // Statistiques pour le tableau de bord
        req.setAttribute("totalClients",    clientDAO.count(null));
        req.setAttribute("totalComptes",    accountDAO.countAll());
        req.setAttribute("soldTotal",       accountDAO.sumSoldes());
        req.setAttribute("operationsJour",  operationDAO.countToday());

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
                .forward(req, resp);
    }
}