<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Compte — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="card shadow-sm" style="max-width: 500px; margin: auto;">
        <div class="card-body">

            <h4 class="mb-4">
                <i class="fas fa-piggy-bank me-2"></i>
                <c:choose>
                    <c:when test="${account.id == 0}">Nouveau compte</c:when>
                    <c:otherwise>Modifier le compte</c:otherwise>
                </c:choose>
            </h4>

            <form method="post"
                  action="${pageContext.request.contextPath}/accounts<c:if test='${account.id != 0}'>/${account.id}/update</c:if>">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Client *</label>
                    <select name="clientId" class="form-select" required
                            <c:if test="${account.id != 0}">disabled</c:if>>
                        <option value="">-- Choisir un client --</option>
                        <c:forEach var="client" items="${clients}">
                            <option value="${client.id}"
                                    <c:if test="${client.id == clientId or client.id == account.client.id}">selected</c:if>>
                                <c:out value="${client.prenom}"/> <c:out value="${client.nom}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <%-- Champ caché pour transmettre clientId quand le select est disabled --%>
                    <c:if test="${account.id != 0}">
                        <input type="hidden" name="clientId" value="${account.client.id}">
                    </c:if>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Type *</label>
                    <select name="type" class="form-select" required>
                        <option value="COURANT" <c:if test="${account.type == 'COURANT'}">selected</c:if>>Compte courant</option>
                        <option value="EPARGNE" <c:if test="${account.type == 'EPARGNE'}">selected</c:if>>Compte épargne</option>
                    </select>
                </div>

                <%-- Dépôt initial uniquement à la création --%>
                <c:if test="${account.id == 0}">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Dépôt initial (FCFA)</label>
                        <input type="number" name="soldeInitial" class="form-control"
                               min="0" step="0.01" placeholder="0">
                    </div>
                </c:if>

                <%-- Statut uniquement à la modification --%>
                <c:if test="${account.id != 0}">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Statut</label>
                        <select name="statut" class="form-select">
                            <option value="ACTIF"   <c:if test="${account.statut == 'ACTIF'}">selected</c:if>>Actif</option>
                            <option value="BLOQUE"  <c:if test="${account.statut == 'BLOQUE'}">selected</c:if>>Bloqué</option>
                            <option value="CLOTURE" <c:if test="${account.statut == 'CLOTURE'}">selected</c:if>>Clôturé</option>
                        </select>
                    </div>
                </c:if>

                <div class="d-flex gap-2 mt-4">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-1"></i>
                        <c:choose>
                            <c:when test="${account.id == 0}">Créer</c:when>
                            <c:otherwise>Enregistrer</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/accounts"
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