<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Détail compte — MicroBank</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    </head>
    <body class="bg-light">

        <jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

        <div class="container">

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="fas fa-piggy-bank me-2"></i>Détail compte</h2>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/accounts/${account.id}/edit"
                       class="btn btn-outline-secondary">
                        <i class="fas fa-pen me-1"></i>Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/accounts"
                       class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-1"></i>Retour
                    </a>
                </div>
            </div>

            <%-- Informations du compte --%>
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <p class="text-muted mb-1">Numéro de compte</p>
                            <p class="fw-semibold"><code><c:out value="${account.numeroCompte}"/></code></p>
                        </div>
                        <div class="col-md-6">
                            <p class="text-muted mb-1">Titulaire</p>
                            <p class="fw-semibold">
                                <c:out value="${account.client.prenom}"/>
                                <c:out value="${account.client.nom}"/>
                            </p>
                        </div>
                        <div class="col-md-6">
                            <p class="text-muted mb-1">Type</p>
                            <p class="fw-semibold"><c:out value="${account.type}"/></p>
                        </div>
                        <div class="col-md-6">
                            <p class="text-muted mb-1">Solde</p>
                            <p class="fw-semibold fs-5 text-success">
                                <c:out value="${account.solde}"/> FCFA
                            </p>
                        </div>
                        <div class="col-md-6">
                            <p class="text-muted mb-1">Statut</p>
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
                        </div>
                    </div>
                </div>
            </div>

            <%-- Boutons d'opérations --%>
            <div class="d-flex gap-2 mb-4">
                <a href="${pageContext.request.contextPath}/operations/deposit?accountId=${account.id}"
                   class="btn btn-success">
                    <i class="fas fa-plus-circle me-1"></i>Dépôt
                </a>
                <a href="${pageContext.request.contextPath}/operations/withdraw?accountId=${account.id}"
                   class="btn btn-danger">
                    <i class="fas fa-minus-circle me-1"></i>Retrait
                </a>
                <a href="${pageContext.request.contextPath}/operations/transfer?accountId=${account.id}"
                   class="btn btn-warning">
                    <i class="fas fa-exchange-alt me-1"></i>Virement
                </a>
                <a href="${pageContext.request.contextPath}/operations?accountId=${account.id}"
                   class="btn btn-outline-secondary">
                    <i class="fas fa-history me-1"></i>Historique
                </a>
            </div>

            <div class="row g-3 mt-3">

                <%-- Formulaire relevé PDF — colonne gauche --%>
                <div class="col-md-6">
                    <div class="card p-3">
                        <h6 class="fw-semibold mb-3">
                            <i class="fas fa-file-pdf text-danger me-1"></i>Relevé PDF
                        </h6>
                        <form method="get"
                              action="${pageContext.request.contextPath}/operations/pdf">
                            <input type="hidden" name="accountId" value="${account.id}">
                            <div class="mb-2">
                                <label class="form-label fw-semibold mb-1">Du</label>
                                <input type="date" name="debut" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold mb-1">Au</label>
                                <input type="date" name="fin" class="form-control" required>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                    <i class="fas fa-file-pdf me-1"></i>Télécharger
                                </button>
                                <a href="${pageContext.request.contextPath}/operations/pdf?accountId=${account.id}&debut=${dateOuvertureStr}&fin=${aujourdhuiStr}"
                                   class="btn btn-outline-secondary btn-sm">
                                    <i class="fas fa-file-pdf me-1"></i>Complet
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <%-- Formulaire export CSV — colonne droite --%>
                <div class="col-md-6">
                    <div class="card p-3">
                        <h6 class="fw-semibold mb-3">
                            <i class="fas fa-file-csv text-success me-1"></i>Export CSV
                        </h6>
                        <form method="get"
                              action="${pageContext.request.contextPath}/operations/csv">
                            <input type="hidden" name="accountId" value="${account.id}">
                            <div class="mb-2">
                                <label class="form-label fw-semibold mb-1">Du</label>
                                <input type="date" name="debut" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold mb-1">Au</label>
                                <input type="date" name="fin" class="form-control" required>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-outline-success btn-sm">
                                    <i class="fas fa-file-csv me-1"></i>Télécharger
                                </button>
                                <a href="${pageContext.request.contextPath}/operations/csv?accountId=${account.id}&debut=${dateOuvertureStr}&fin=${aujourdhuiStr}"
                                   class="btn btn-outline-secondary btn-sm">
                                    <i class="fas fa-file-csv me-1"></i>Complet
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

            </div>

            <%-- Message si aucune opération sur la période --%>
            <c:if test="${not empty param.noData}">
                <div class="alert alert-warning mt-3">
                    <i class="fas fa-exclamation-triangle me-1"></i>
                    Aucune opération trouvée sur cette période.
                </div>
            </c:if>
        </div>
    </body>
</html>