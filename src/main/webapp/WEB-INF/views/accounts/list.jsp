<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Comptes — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-piggy-bank me-2"></i>Comptes</h2>
    </div>

    <c:choose>
        <c:when test="${empty accounts}">
            <div class="text-center py-5">
                <i class="fas fa-piggy-bank fa-4x text-muted mb-3"></i>
                <p class="text-muted fs-5">Aucun compte enregistré.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-striped table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>Numéro</th>
                            <th>Client</th>
                            <th>Type</th>
                            <th class="text-end">Solde</th>
                            <th class="text-center">Statut</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="account" items="${accounts}">
                            <tr>
                                <td><code><c:out value="${account.numeroCompte}"/></code></td>
                                <td>
                                    <c:out value="${account.client.prenom}"/>
                                    <c:out value="${account.client.nom}"/>
                                </td>
                                <td><c:out value="${account.type}"/></td>
                                <td class="text-end fw-semibold">
                                    <c:out value="${account.solde}"/> FCFA
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${account.statut == 'ACTIF'}">
                                            <span class="badge bg-success">Actif</span>
                                        </c:when>
                                        <c:when test="${account.statut == 'BLOQUE'}">
                                            <span class="badge bg-warning text-dark">Bloqué</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">Clôturé</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/accounts/${account.id}/details"
                                       class="btn btn-sm btn-outline-info me-1">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/accounts/${account.id}/edit"
                                       class="btn btn-sm btn-outline-secondary">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <p class="text-muted mt-2">
                <i class="fas fa-info-circle me-1"></i>
                <strong>${accounts.size()}</strong> compte(s) au total
            </p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>