<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Virement — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">
    <div class="card shadow-sm" style="max-width: 500px; margin: auto;">
        <div class="card-body">

            <h4 class="mb-4">
                <i class="fas fa-exchange-alt text-warning me-2"></i>Virement
            </h4>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle me-1"></i>
                    <c:out value="${param.error}"/>
                </div>
            </c:if>

            <div class="alert alert-info mb-4">
                <p class="mb-1"><strong>Compte source :</strong>
                    <c:out value="${account.numeroCompte}"/>
                </p>
                <p class="mb-0"><strong>Solde actuel :</strong>
                    <c:out value="${account.solde}"/> FCFA
                </p>
            </div>

            <form method="post"
                  action="${pageContext.request.contextPath}/operations/transfer">

                <input type="hidden" name="accountId" value="${account.id}">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Compte destination *</label>
                    <select name="accountDestId" class="form-select" required>
                        <option value="">-- Choisir un compte --</option>
                        <c:forEach var="acc" items="${accounts}">
                            <%-- On exclut le compte source du dropdown --%>
                            <c:if test="${acc.id != account.id}">
                                <option value="${acc.id}">
                                    <c:out value="${acc.numeroCompte}"/> —
                                    <c:out value="${acc.client.prenom}"/>
                                    <c:out value="${acc.client.nom}"/>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Montant (FCFA) *</label>
                    <input type="number" name="montant" class="form-control"
                           min="1" step="0.01" required placeholder="0">
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold">Description</label>
                    <input type="text" name="description" class="form-control"
                           placeholder="Ex: Remboursement">
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-warning">
                        <i class="fas fa-check me-1"></i>Confirmer
                    </button>
                    <a href="${pageContext.request.contextPath}/accounts/${account.id}/details"
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