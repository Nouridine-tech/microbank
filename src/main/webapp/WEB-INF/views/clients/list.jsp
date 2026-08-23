<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Clients — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2><i class="fas fa-users me-2"></i>Clients</h2>
        <a href="${pageContext.request.contextPath}/clients/new" class="btn btn-primary">
            <i class="fas fa-plus me-1"></i>Nouveau client
        </a>
    </div>

    <%-- Barre de recherche --%>
    <form method="get" action="${pageContext.request.contextPath}/clients" class="mb-4">
        <div class="row g-2 align-items-end">
            <div class="col-md-10">
                <input type="text"
                       name="search"
                       class="form-control"
                       placeholder="Rechercher par nom, prénom, téléphone ou numéro de pièce..."
                       value="${search}">
            </div>
            <div class="col-auto d-flex gap-2">
                <button type="submit" class="btn btn-outline-secondary">
                    <i class="fas fa-search"></i>
                </button>
                <c:if test="${not empty search}">
                    <a href="${pageContext.request.contextPath}/clients"
                       class="btn btn-outline-danger">
                        <i class="fas fa-rotate-left"></i>
                    </a>
                </c:if>
            </div>
        </div>
    </form>

    <%-- Tableau des clients --%>
    <c:choose>
        <c:when test="${empty clients}">
            <div class="text-center py-5">
                <i class="fas fa-users fa-4x text-muted mb-3"></i>
                <c:choose>
                    <c:when test="${not empty search}">
                        <p class="text-muted fs-5">Aucun client trouvé.</p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted fs-5">Aucun client enregistré.</p>
                        <a href="${pageContext.request.contextPath}/clients/new"
                           class="btn btn-primary">
                            <i class="fas fa-plus me-1"></i>Ajouter le premier client
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-striped table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Téléphone</th>
                            <th>Email</th>
                            <th class="text-center">Statut</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="client" items="${clients}">
                            <tr>
                                <td class="fw-semibold"><c:out value="${client.nom}"/></td>
                                <td><c:out value="${client.prenom}"/></td>
                                <td><c:out value="${client.telephone}"/></td>
                                <td><c:out value="${client.email}"/></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${client.statut == 'ACTIF'}">
                                            <span class="badge bg-success">Actif</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">Inactif</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center text-nowrap">
                                    <a href="${pageContext.request.contextPath}/clients/${client.id}/details"
                                       class="btn btn-sm btn-outline-info me-1">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/clients/${client.id}/edit"
                                       class="btn btn-sm btn-outline-secondary me-1">
                                        <i class="fas fa-pen"></i>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/clients/${client.id}/delete"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Supprimer ce client ?')">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <%-- Compteur et pagination --%>
            <div class="d-flex justify-content-between align-items-center mt-2">
                <p class="text-muted mb-0">
                    <i class="fas fa-info-circle me-1"></i>
                    <strong>${total}</strong> client(s) — page ${currentPage} / ${totalPages}
                </p>
                <c:if test="${totalPages > 1}">
                    <nav>
                        <ul class="pagination mb-0">
                            <li class="page-item <c:if test='${currentPage <= 1}'>disabled</c:if>">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/clients?page=${currentPage - 1}&search=${search}">
                                    &laquo;
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/clients?page=${i}&search=${search}">
                                            ${i}
                                    </a>
                                </li>
                            </c:forEach>
                            <li class="page-item <c:if test='${currentPage >= totalPages}'>disabled</c:if>">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/clients?page=${currentPage + 1}&search=${search}">
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