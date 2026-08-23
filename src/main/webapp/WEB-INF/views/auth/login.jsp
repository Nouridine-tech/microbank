<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MicroBank — Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-dark d-flex align-items-center justify-content-center min-vh-100">

<div class="card shadow" style="width: 380px;">
    <div class="card-body p-4">

        <%-- En-tête avec icône et titre --%>
        <div class="text-center mb-4">
            <i class="fas fa-landmark fa-3x text-primary mb-2"></i>
            <h4 class="fw-bold">MicroBank</h4>
            <p class="text-muted small">Système de gestion</p>
        </div>

        <%-- Message d'erreur affiché si les identifiants sont incorrects --%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">
                <i class="fas fa-exclamation-circle me-1"></i>
                <c:out value="${error}"/>
            </div>
        </c:if>

        <%-- Formulaire de connexion — POST vers AuthServlet --%>
        <form method="post" action="${pageContext.request.contextPath}/auth/login">

            <div class="mb-3">
                <label class="form-label fw-semibold">
                    <i class="fas fa-user me-1"></i>Identifiant
                </label>
                <input type="text"
                       name="login"
                       class="form-control"
                       placeholder="Votre identifiant"
                       required>
            </div>

            <div class="mb-4">
                <label class="form-label fw-semibold">
                    <i class="fas fa-lock me-1"></i>Mot de passe
                </label>
                <input type="password"
                       name="password"
                       class="form-control"
                       placeholder="Votre mot de passe"
                       required>
            </div>

            <button type="submit" class="btn btn-primary w-100">
                <i class="fas fa-sign-in-alt me-1"></i>Se connecter
            </button>

        </form>

        <%-- Compte de test affiché en bas--%>
        <p class="text-center text-muted small mt-3 mb-0">
            Compte par défaut : <strong>admin</strong> / <strong>admin123</strong>
        </p>

    </div>
</div>

</body>
</html>