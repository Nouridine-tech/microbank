package isi.nour.microbank.controller;

import isi.nour.microbank.dao.AccountDAO;
import isi.nour.microbank.dao.OperationDAO;
import isi.nour.microbank.model.Account;
import isi.nour.microbank.model.Operation;
import isi.nour.microbank.model.User;
import isi.nour.microbank.service.CsvService;
import isi.nour.microbank.service.OperationService;
import isi.nour.microbank.service.PdfService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/operations/*")
public class OperationServlet extends HttpServlet {

    private final OperationService operationService = new OperationService();
    private final OperationDAO     operationDAO     = new OperationDAO();
    private final AccountDAO       accountDAO       = new AccountDAO();
    private final PdfService pdfService = new PdfService();
    private final CsvService csvService = new CsvService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Historique des opérations d'un compte
            listOperations(req, resp);
        } else if (pathInfo.equals("/deposit")) {
            showDeposit(req, resp);
        } else if (pathInfo.equals("/withdraw")) {
            showWithdraw(req, resp);
        } else if (pathInfo.equals("/transfer")) {
            showTransfer(req, resp);
        } else if (pathInfo.equals("/pdf")) {
            generatePdf(req, resp);
        } else if (pathInfo.equals("/csv")) {
        generateCsv(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (pathInfo) {
            case "/deposit":
                processDeposit(req, resp);
                break;
            case "/withdraw":
                processWithdraw(req, resp);
                break;
            case "/transfer":
                processTransfer(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Récupère l'agent connecté depuis la session
    private User getAgent(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (User) session.getAttribute("user");
    }

    private void listOperations(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accountIdParam = req.getParameter("accountId");

        // Si accountId est absent, on redirige vers la liste des comptes
        if (accountIdParam == null || accountIdParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/accounts");
            return;
        }

        int accountId = Integer.parseInt(accountIdParam);

        // Filtres optionnels
        String type      = req.getParameter("type");
        String debutStr  = req.getParameter("debut");
        String finStr    = req.getParameter("fin");
        String pageParam = req.getParameter("page");

        LocalDateTime debut = (debutStr != null && !debutStr.isEmpty())
                ? LocalDateTime.parse(debutStr + "T00:00:00") : null;
        LocalDateTime fin   = (finStr != null && !finStr.isEmpty())
                ? LocalDateTime.parse(finStr + "T23:59:59") : null;

        int page     = 1;
        int pageSize = 10;
        if (pageParam != null && !pageParam.isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException e) { page = 1; }
            if (page < 1) page = 1;
        }

        int offset     = (page - 1) * pageSize;
        long total     = operationDAO.countByAccount(accountId, type, debut, fin);
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Operation> operations = operationDAO.findByAccountPaged(
                accountId, type, debut, fin, pageSize, offset);

        Account account = accountDAO.findById(accountId);

        req.setAttribute("operations",   operations);
        req.setAttribute("account",      account);
        req.setAttribute("currentPage",  page);
        req.setAttribute("totalPages",   totalPages);
        req.setAttribute("total",        total);
        req.setAttribute("filterType",   type   != null ? type   : "");
        req.setAttribute("filterDebut",  debutStr != null ? debutStr : "");
        req.setAttribute("filterFin",    finStr   != null ? finStr   : "");

        req.getRequestDispatcher("/WEB-INF/views/operations/list.jsp")
                .forward(req, resp);
    }

    private void showDeposit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int accountId    = Integer.parseInt(req.getParameter("accountId"));
        Account account  = accountDAO.findById(accountId);
        req.setAttribute("account", account);
        req.getRequestDispatcher("/WEB-INF/views/operations/deposit.jsp")
                .forward(req, resp);
    }

    private void showWithdraw(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int accountId    = Integer.parseInt(req.getParameter("accountId"));
        Account account  = accountDAO.findById(accountId);
        req.setAttribute("account", account);
        req.getRequestDispatcher("/WEB-INF/views/operations/withdraw.jsp")
                .forward(req, resp);
    }

    private void showTransfer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int accountId          = Integer.parseInt(req.getParameter("accountId"));
        Account account        = accountDAO.findById(accountId);
        List<Account> accounts = accountDAO.findAllWithClient();
        req.setAttribute("account",  account);
        req.setAttribute("accounts", accounts);
        req.getRequestDispatcher("/WEB-INF/views/operations/transfer.jsp")
                .forward(req, resp);
    }

    private void processDeposit(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int accountId      = Integer.parseInt(req.getParameter("accountId"));
        BigDecimal montant = new BigDecimal(req.getParameter("montant"));
        String description = req.getParameter("description");

        try {
            operationService.depot(accountId, montant, description, getAgent(req));
            resp.sendRedirect(req.getContextPath() + "/accounts/" + accountId + "/details");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() +
                    "/operations/deposit?accountId=" + accountId + "&error=" + e.getMessage());
        }
    }

    private void processWithdraw(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int accountId      = Integer.parseInt(req.getParameter("accountId"));
        BigDecimal montant = new BigDecimal(req.getParameter("montant"));
        String description = req.getParameter("description");

        try {
            operationService.retrait(accountId, montant, description, getAgent(req));
            resp.sendRedirect(req.getContextPath() + "/accounts/" + accountId + "/details");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() +
                    "/operations/withdraw?accountId=" + accountId + "&error=" + e.getMessage());
        }
    }

    private void processTransfer(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int accountSourceId = Integer.parseInt(req.getParameter("accountId"));
        int accountDestId   = Integer.parseInt(req.getParameter("accountDestId"));
        BigDecimal montant  = new BigDecimal(req.getParameter("montant"));
        String description  = req.getParameter("description");

        try {
            operationService.virement(accountSourceId, accountDestId,
                    montant, description, getAgent(req));
            resp.sendRedirect(req.getContextPath() + "/accounts/" + accountSourceId + "/details");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() +
                    "/operations/transfer?accountId=" + accountSourceId + "&error=" + e.getMessage());
        }
    }

    // Methode pour generer des PDF de relevés
    private void generatePdf(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int accountId = Integer.parseInt(req.getParameter("accountId"));

        String debutStr = req.getParameter("debut");
        String finStr   = req.getParameter("fin");

        // Vérification que les dates ne sont pas vides — AVANT le parse
        if (debutStr == null || debutStr.isEmpty() ||
                finStr   == null || finStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/accounts/" + accountId + "/details?noData=true");
            return;
        }

        Account account = accountDAO.findById(accountId);

        java.time.LocalDateTime debut = java.time.LocalDate.parse(debutStr).atStartOfDay();
        java.time.LocalDateTime fin   = java.time.LocalDate.parse(finStr).atTime(23, 59, 59);

        List<Operation> operations = operationDAO.findByAccountPaged(
                accountId, null, debut, fin, Integer.MAX_VALUE, 0);

        // Si aucune opération sur la période, on redirige avec un message
        if (operations.isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/accounts/" + accountId + "/details?noData=true");
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"releve-" + account.getNumeroCompte() + ".pdf\"");

        pdfService.genererReleve(account, operations, debutStr, finStr,
                resp.getOutputStream());
    }

    // Methode pour generer des Csv
    private void generateCsv(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int accountId = Integer.parseInt(req.getParameter("accountId"));

        String debutStr = req.getParameter("debut");
        String finStr   = req.getParameter("fin");

        // Vérification que les dates ne sont pas vides
        if (debutStr == null || debutStr.isEmpty() ||
                finStr   == null || finStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/accounts/" + accountId + "/details?noData=true");
            return;
        }

        Account account = accountDAO.findById(accountId);

        java.time.LocalDateTime debut = java.time.LocalDate.parse(debutStr).atStartOfDay();
        java.time.LocalDateTime fin   = java.time.LocalDate.parse(finStr).atTime(23, 59, 59);

        List<Operation> operations = operationDAO.findByAccountPaged(
                accountId, null, debut, fin, Integer.MAX_VALUE, 0);

        // Si aucune opération sur la période, on redirige avec un message
        if (operations.isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/accounts/" + accountId + "/details?noData=true");
            return;
        }

        // On indique au navigateur que la réponse est un fichier CSV à télécharger
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"operations-" + account.getNumeroCompte() + ".csv\"");

        csvService.exporterOperations(operations, resp.getOutputStream());
    }
}