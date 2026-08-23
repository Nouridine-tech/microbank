<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Client — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="card shadow-sm" style="max-width: 600px; margin: auto;">
        <div class="card-body">

            <h4 class="mb-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-1"></i>
                        <c:out value="${error}"/>
                    </div>
                </c:if>
                <i class="fas fa-user-plus me-2"></i>
                <c:choose>
                    <c:when test="${client.id == 0}">Nouveau client</c:when>
                    <c:otherwise>Modifier le client</c:otherwise>
                </c:choose>
            </h4>

            <%-- Action dynamique : POST /clients pour créer, POST /clients/{id}/update pour modifier --%>
            <form method="post"
                  action="${pageContext.request.contextPath}/clients<c:if test='${client.id != 0}'>/${client.id}/update</c:if>">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Nom *</label>
                        <input type="text" name="nom" class="form-control"
                               value="${client.nom}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Prénom *</label>
                        <input type="text" name="prenom" class="form-control"
                               value="${client.prenom}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Téléphone *</label>
                        <input type="text" name="telephone" class="form-control"
                               value="${client.telephone}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Email</label>
                        <input type="email" name="email" class="form-control"
                               value="${client.email}">
                    </div>
                    <div class="col-12">
                        <label class="form-label fw-semibold">Adresse</label>
                        <input type="text" name="adresse" class="form-control"
                               value="${client.adresse}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Numéro de pièce *</label>
                        <input type="text" name="numeroPiece" class="form-control"
                               value="${client.numeroPiece}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Date de naissance</label>
                        <input type="date" name="dateNaissance" class="form-control"
                               value="${client.dateNaissance}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Statut</label>
                        <select name="statut" class="form-select">
                            <option value="ACTIF"   <c:if test="${client.statut == 'ACTIF'}">selected</c:if>>Actif</option>
                            <option value="INACTIF" <c:if test="${client.statut == 'INACTIF'}">selected</c:if>>Inactif</option>
                        </select>
                    </div>
                </div>

                <div class="d-flex gap-2 mt-4">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-1"></i>
                        <c:choose>
                            <c:when test="${client.id == 0}">Créer</c:when>
                            <c:otherwise>Enregistrer</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/clients"
                       class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Annuler
                    </a>
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>