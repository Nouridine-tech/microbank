<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Utilisateurs — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-user-cog me-2"></i>Utilisateurs</h2>
        <a href="${pageContext.request.contextPath}/users/new"
           class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouvel utilisateur
        </a>
    </div>

    <%-- Message d'erreur si suppression impossible --%>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle me-1"></i>
            <c:out value="${param.error}"/>
        </div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                <tr>
                    <th>Identifiant</th>
                    <th>Nom complet</th>
                    <th class="text-center">Rôle</th>
                    <th class="text-center">Statut</th>
                    <th class="text-center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td class="fw-semibold"><c:out value="${u.login}"/></td>
                        <td>
                            <c:out value="${u.prenom}"/>
                            <c:out value="${u.nom}"/>
                        </td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}">
                                    <span class="badge bg-danger">ADMIN</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-primary">AGENT</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${u.actif}">
                                    <span class="badge bg-success">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactif</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center text-nowrap">
                            <a href="${pageContext.request.contextPath}/users/${u.id}/edit"
                               class="btn btn-sm btn-outline-secondary me-1">
                                <i class="fas fa-pen"></i>
                            </a>
                                <%-- On ne peut pas supprimer son propre compte --%>
                            <c:if test="${u.id != sessionScope.user.id}">
                                <form action="${pageContext.request.contextPath}/users/${u.id}/delete"
                                      method="post" class="d-inline"
                                      onsubmit="return confirm('Supprimer cet utilisateur ?')">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <p class="text-muted mt-2">
        <i class="fas fa-info-circle me-1"></i>
        <strong>${users.size()}</strong> utilisateur(s)
    </p>
</div>
</body>
</html>