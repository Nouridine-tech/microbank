<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Dashboard — MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/tools/_navbar.jsp"/>

<div class="container">

    <h2 class="mb-4">
        <i class="fas fa-tachometer-alt me-2"></i>Tableau de bord
    </h2>

    <div class="row g-4">

        <%-- Carte : Clients --%>
        <div class="col-md-3">
            <div class="card shadow-sm text-center">
                <div class="card-body py-4">
                    <i class="fas fa-users fa-2x text-primary mb-3"></i>
                    <h3 class="fw-bold"><c:out value="${totalClients}"/></h3>
                    <p class="text-muted mb-0">Clients</p>
                </div>
            </div>
        </div>

        <%-- Carte : Comptes --%>
        <div class="col-md-3">
            <div class="card shadow-sm text-center">
                <div class="card-body py-4">
                    <i class="fas fa-piggy-bank fa-2x text-success mb-3"></i>
                    <h3 class="fw-bold"><c:out value="${totalComptes}"/></h3>
                    <p class="text-muted mb-0">Comptes</p>
                </div>
            </div>
        </div>

        <%-- Carte : Solde total --%>
        <div class="col-md-3">
            <div class="card shadow-sm text-center">
                <div class="card-body py-4">
                    <i class="fas fa-coins fa-2x text-warning mb-3"></i>
                    <h3 class="fw-bold"><c:out value="${soldTotal}"/></h3>
                    <p class="text-muted mb-0">Solde total (FCFA)</p>
                </div>
            </div>
        </div>

        <%-- Carte : Opérations du jour --%>
        <div class="col-md-3">
            <div class="card shadow-sm text-center">
                <div class="card-body py-4">
                    <i class="fas fa-exchange-alt fa-2x text-danger mb-3"></i>
                    <h3 class="fw-bold"><c:out value="${operationsJour}"/></h3>
                    <p class="text-muted mb-0">Opérations du jour</p>
                </div>
            </div>
        </div>

    </div>
</div>
</body>
</html>