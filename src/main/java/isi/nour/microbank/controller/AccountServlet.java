package isi.nour.microbank.controller;

import isi.nour.microbank.dao.AccountDAO;
import isi.nour.microbank.dao.ClientDAO;
import isi.nour.microbank.model.Account;
import isi.nour.microbank.model.Client;
import isi.nour.microbank.model.User;
import isi.nour.microbank.utils.Tools;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/accounts/*")
public class AccountServlet extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();
    private final ClientDAO  clientDAO  = new ClientDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listAccounts(req, resp);
        } else if (pathInfo.equals("/new")) {
            showForm(req, resp, new Account());
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                Account account = accountDAO.findById(id);
                if (account != null) {
                    showForm(req, resp, account);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else if (parts.length == 3 && parts[2].equals("details")) {
            int id = Integer.parseInt(parts[1]);
            Account account = accountDAO.findById(id);
            if (account != null) {
                req.setAttribute("account", account);
                // Dates passées à la JSP pour le bouton "Relevé complet"
                req.setAttribute("dateOuvertureStr",
                        account.getDateOuverture().toLocalDate().toString());
                req.setAttribute("aujourdhuiStr",
                        java.time.LocalDate.now().toString());
                req.getRequestDispatcher("/WEB-INF/views/accounts/details.jsp")
                        .forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            createAccount(req, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update":
                        updateAccount(req, resp, id);
                        break;
                    case "delete":
                        accountDAO.delete(id);
                        resp.sendRedirect(req.getContextPath() + "/accounts");
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    private void listAccounts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Account> accounts = accountDAO.findAll();
        req.setAttribute("accounts", accounts);
        req.getRequestDispatcher("/WEB-INF/views/accounts/list.jsp")
                .forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Account account)
            throws ServletException, IOException {

        // On passe l'id client si on vient de la page détail client
        String clientId = req.getParameter("clientId");

        List<Client> clients = clientDAO.findAll();
        req.setAttribute("clients",  clients);
        req.setAttribute("account",  account);
        req.setAttribute("clientId", clientId);

        req.getRequestDispatcher("/WEB-INF/views/accounts/form.jsp")
                .forward(req, resp);
    }

    private void createAccount(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Account account = new Account();

        // Génère un numéro de compte unique
        String numero = Tools.generateAccountNumber();
        while (accountDAO.existsByNumeroCompte(numero)) {
            numero = Tools.generateAccountNumber();
        }
        account.setNumeroCompte(numero);
        account.setType(req.getParameter("type"));
        account.setStatut("ACTIF");

        // Solde initial optionnel
        String soldeParam = req.getParameter("soldeInitial");
        if (soldeParam != null && !soldeParam.isEmpty()) {
            account.setSolde(new BigDecimal(soldeParam));
        } else {
            account.setSolde(BigDecimal.ZERO);
        }

        // Rattache le client
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        account.setClient(clientDAO.findById(clientId));

        accountDAO.save(account);
        resp.sendRedirect(req.getContextPath() + "/clients/" + clientId + "/details");
    }

    private void updateAccount(HttpServletRequest req, HttpServletResponse resp, int id)
            throws IOException {

        Account account = accountDAO.findById(id);
        if (account == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        account.setType(req.getParameter("type"));
        account.setStatut(req.getParameter("statut"));

        accountDAO.update(account);
        resp.sendRedirect(req.getContextPath() + "/accounts/" + id + "/details");
    }
}