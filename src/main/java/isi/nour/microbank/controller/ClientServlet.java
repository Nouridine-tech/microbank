package isi.nour.microbank.controller;

import isi.nour.microbank.dao.ClientDAO;
import isi.nour.microbank.model.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/clients/*")
public class ClientServlet extends HttpServlet {

    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listClients(req, resp);
        } else if (pathInfo.equals("/new")) {
            showForm(req, resp, new Client());
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3 && parts[2].equals("edit")) {
                int id = Integer.parseInt(parts[1]);
                Client client = clientDAO.findById(id);
                if (client != null) {
                    showForm(req, resp, client);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else if (parts.length == 3 && parts[2].equals("details")) {
                int id = Integer.parseInt(parts[1]);
                // On utilise findByIdWithAccounts au lieu de findById
                // pour charger les comptes dans la même session JPA
                Client client = clientDAO.findByIdWithAccounts(id);
                if (client != null) {
                    req.setAttribute("client", client);
                    req.getRequestDispatcher("/WEB-INF/views/clients/details.jsp")
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
            createClient(req, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[1]);
                switch (parts[2]) {
                    case "update":
                        updateClient(req, resp, id);
                        break;
                    case "delete":
                        clientDAO.delete(id);
                        resp.sendRedirect(req.getContextPath() + "/clients");
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    private void listClients(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword  = req.getParameter("search");
        String pageParam = req.getParameter("page");

        int page     = 1;
        int pageSize = 10;

        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        long total      = clientDAO.count(keyword);
        int totalPages  = (int) Math.ceil((double) total / pageSize);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Client> clients = clientDAO.findPaged(keyword, pageSize, offset);

        req.setAttribute("clients",     clients);
        req.setAttribute("search",      keyword != null ? keyword : "");
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages",  totalPages);
        req.setAttribute("total",       total);

        req.getRequestDispatcher("/WEB-INF/views/clients/list.jsp")
                .forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Client client)
            throws ServletException, IOException {
        req.setAttribute("client", client);
        req.getRequestDispatcher("/WEB-INF/views/clients/form.jsp")
                .forward(req, resp);
    }

    private void createClient(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String telephone   = req.getParameter("telephone");
        String numeroPiece = req.getParameter("numeroPiece");

        // Vérification unicité téléphone
        if (clientDAO.existsByTelephone(telephone, 0)) {
            Client client = new Client();
            extractClient(req, client);
            req.setAttribute("error", "Ce numéro de téléphone est déjà utilisé.");
            showForm(req, resp, client);
            return;
        }

        // Vérification unicité numéro de pièce
        if (clientDAO.existsByNumeroPiece(numeroPiece, 0)) {
            Client client = new Client();
            extractClient(req, client);
            req.setAttribute("error", "Ce numéro de pièce est déjà utilisé.");
            showForm(req, resp, client);
            return;
        }

        Client client = new Client();
        extractClient(req, client);
        clientDAO.save(client);
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    private void updateClient(HttpServletRequest req, HttpServletResponse resp, int id)
            throws ServletException, IOException {

        Client client = clientDAO.findById(id);
        if (client == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String telephone   = req.getParameter("telephone");
        String numeroPiece = req.getParameter("numeroPiece");

        // Vérification unicité téléphone — on exclut le client en cours de modification
        if (clientDAO.existsByTelephone(telephone, id)) {
            extractClient(req, client);
            req.setAttribute("error", "Ce numéro de téléphone est déjà utilisé.");
            showForm(req, resp, client);
            return;
        }

        // Vérification unicité numéro de pièce — on exclut le client en cours de modification
        if (clientDAO.existsByNumeroPiece(numeroPiece, id)) {
            extractClient(req, client);
            req.setAttribute("error", "Ce numéro de pièce est déjà utilisé.");
            showForm(req, resp, client);
            return;
        }

        extractClient(req, client);
        clientDAO.update(client);
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    private void extractClient(HttpServletRequest req, Client client) {
        client.setNom(req.getParameter("nom"));
        client.setPrenom(req.getParameter("prenom"));
        client.setTelephone(req.getParameter("telephone"));
        client.setEmail(req.getParameter("email"));
        client.setAdresse(req.getParameter("adresse"));
        client.setNumeroPiece(req.getParameter("numeroPiece"));
        client.setStatut(req.getParameter("statut"));

        // dateNaissance arrive en String "yyyy-MM-dd" depuis le formulaire HTML
        String dateNaissance = req.getParameter("dateNaissance");
        if (dateNaissance != null && !dateNaissance.isEmpty()) {
            client.setDateNaissance(LocalDate.parse(dateNaissance));
        }
    }
}