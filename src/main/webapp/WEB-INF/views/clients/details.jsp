<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détail client — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-user me-2"></i>Détail client</h2>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/clients/${client.id}/edit"
               class="btn btn-outline-secondary">
                <i class="fas fa-pen me-1"></i>Modifier
            </a>
            <a href="${pageContext.request.contextPath}/clients"
               class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-1"></i>Retour
            </a>
        </div>
    </div>

    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <div class="row g-3">
                <div class="col-md-6">
                    <p class="text-muted mb-1">Nom complet</p>
                    <p class="fw-semibold"><c:out value="${client.prenom}"/> <c:out value="${client.nom}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Téléphone</p>
                    <p class="fw-semibold"><c:out value="${client.telephone}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Email</p>
                    <p class="fw-semibold"><c:out value="${client.email}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Adresse</p>
                    <p class="fw-semibold"><c:out value="${client.adresse}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Numéro de pièce</p>
                    <p class="fw-semibold"><c:out value="${client.numeroPiece}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Date de naissance</p>
                    <p class="fw-semibold"><c:out value="${client.dateNaissance}"/></p>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Statut</p>
                    <c:choose>
                        <c:when test="${client.statut == 'ACTIF'}">
                            <span class="badge bg-success">Actif</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Inactif</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-md-6">
                    <p class="text-muted mb-1">Date de création</p>
                    <p class="fw-semibold"><c:out value="${client.dateCreation}"/></p>
                </div>
            </div>
        </div>
    </div>

    <%-- Comptes du client --%>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-piggy-bank me-2"></i>Comptes</h4>
        <a href="${pageContext.request.contextPath}/accounts/new?clientId=${client.id}"
           class="btn btn-sm btn-primary">
            <i class="fas fa-plus me-1"></i>Ouvrir un compte
        </a>
    </div>

    <c:choose>
        <c:when test="${empty client.accounts}">
            <p class="text-muted">Aucun compte pour ce client.</p>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>Numéro</th>
                            <th>Type</th>
                            <th class="text-end">Solde</th>
                            <th class="text-center">Statut</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="account" items="${client.accounts}">
                            <tr>
                                <td><code><c:out value="${account.numeroCompte}"/></code></td>
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
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/accounts/${account.id}/details"
                                       class="btn btn-sm btn-outline-info">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>