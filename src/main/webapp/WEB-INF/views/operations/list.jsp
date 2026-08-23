<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Historique — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-history me-2"></i>Historique des opérations</h2>
        <a href="${pageContext.request.contextPath}/accounts/${account.id}/details"
           class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i>Retour
        </a>
    </div>

    <%-- Informations du compte --%>
    <div class="alert alert-info mb-3">
        <strong>Compte :</strong> <c:out value="${account.numeroCompte}"/> —
        <strong>Solde :</strong> <c:out value="${account.solde}"/> FCFA
    </div>

    <%-- Filtres --%>
    <form method="get"
          action="${pageContext.request.contextPath}/operations"
          class="mb-4">
        <input type="hidden" name="accountId" value="${account.id}">
        <div class="row g-2 align-items-end">
            <div class="col-md-3">
                <label class="form-label fw-semibold">Type</label>
                <select name="type" class="form-select">
                    <option value="">Tous</option>
                    <option value="DEPOT"    <c:if test="${filterType == 'DEPOT'}">selected</c:if>>Dépôt</option>
                    <option value="RETRAIT"  <c:if test="${filterType == 'RETRAIT'}">selected</c:if>>Retrait</option>
                    <option value="VIREMENT" <c:if test="${filterType == 'VIREMENT'}">selected</c:if>>Virement</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold">Du</label>
                <input type="date" name="debut" class="form-control"
                       value="${filterDebut}">
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold">Au</label>
                <input type="date" name="fin" class="form-control"
                       value="${filterFin}">
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-outline-secondary">
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </div>
    </form>

    <c:choose>
        <c:when test="${empty operations}">
            <div class="text-center py-5">
                <i class="fas fa-history fa-4x text-muted mb-3"></i>
                <p class="text-muted fs-5">Aucune opération trouvée.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-striped table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>Date</th>
                            <th>Référence</th>
                            <th>Type</th>
                            <th class="text-end">Montant</th>
                            <th class="text-end">Solde après</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="op" items="${operations}">
                            <tr>
                                <td><c:out value="${op.dateOperation}"/></td>
                                <td><code><c:out value="${op.reference}"/></code></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${op.type == 'DEPOT'}">
                                            <span class="badge bg-success">Dépôt</span>
                                        </c:when>
                                        <c:when test="${op.type == 'RETRAIT'}">
                                            <span class="badge bg-danger">Retrait</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark">Virement</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end fw-semibold">
                                    <c:out value="${op.montant}"/> FCFA
                                </td>
                                <td class="text-end">
                                    <c:out value="${op.soldeApres}"/> FCFA
                                </td>
                                <td><c:out value="${op.description}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="d-flex justify-content-between align-items-center mt-2">
                <p class="text-muted mb-0">
                    <i class="fas fa-info-circle me-1"></i>
                    <strong>${total}</strong> opération(s) — page ${currentPage} / ${totalPages}
                </p>
                <c:if test="${totalPages > 1}">
                    <nav>
                        <ul class="pagination mb-0">
                            <li class="page-item <c:if test='${currentPage <= 1}'>disabled</c:if>">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/operations?accountId=${account.id}&page=${currentPage - 1}&type=${filterType}&debut=${filterDebut}&fin=${filterFin}">
                                    &laquo;
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/operations?accountId=${account.id}&page=${i}&type=${filterType}&debut=${filterDebut}&fin=${filterFin}">
                                            ${i}
                                    </a>
                                </li>
                            </c:forEach>
                            <li class="page-item <c:if test='${currentPage >= totalPages}'>disabled</c:if>">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/operations?accountId=${account.id}&page=${currentPage + 1}&type=${filterType}&debut=${filterDebut}&fin=${filterFin}">
                                    &raquo;
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>