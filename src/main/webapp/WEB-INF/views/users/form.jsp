<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Utilisateur — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="card shadow-sm" style="max-width: 500px; margin: auto;">
        <div class="card-body">

            <h4 class="mb-4">
                <i class="fas fa-user-plus me-2"></i>
                <c:choose>
                    <c:when test="${user.id == 0}">Nouvel utilisateur</c:when>
                    <c:otherwise>Modifier l'utilisateur</c:otherwise>
                </c:choose>
            </h4>

            <form method="post"
                  action="${pageContext.request.contextPath}/users<c:if test='/${user.id != 0}'>/${user.id}/update</c:if>">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Nom *</label>
                        <input type="text" name="nom" class="form-control"
                               value="${user.nom}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Prénom *</label>
                        <input type="text" name="prenom" class="form-control"
                               value="${user.prenom}" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label fw-semibold">Identifiant *</label>
                        <input type="text" name="login" class="form-control"
                               value="${user.login}" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label fw-semibold">
                            Mot de passe
                            <c:if test="${user.id != 0}">
                                <span class="text-muted fw-normal">(laisser vide pour ne pas changer)</span>
                            </c:if>
                            <c:if test="${user.id == 0}">*</c:if>
                        </label>
                        <input type="password" name="password" class="form-control"
                               <c:if test="${user.id == 0}">required</c:if>>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Rôle *</label>
                        <select name="role" class="form-select" required>
                            <option value="AGENT" <c:if test="${user.role == 'AGENT'}">selected</c:if>>Agent</option>
                            <option value="ADMIN" <c:if test="${user.role == 'ADMIN'}">selected</c:if>>Admin</option>
                        </select>
                    </div>
                    <div class="col-md-6 d-flex align-items-end">
                        <div class="form-check">
                            <input type="checkbox" name="actif" class="form-check-input"
                                   id="actif" <c:if test="${user.actif or user.id == 0}">checked</c:if>>
                            <label class="form-check-label" for="actif">Compte actif</label>
                        </div>
                    </div>
                </div>

                <div class="d-flex gap-2 mt-4">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-1"></i>
                        <c:choose>
                            <c:when test="${user.id == 0}">Créer</c:when>
                            <c:otherwise>Enregistrer</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/users"
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