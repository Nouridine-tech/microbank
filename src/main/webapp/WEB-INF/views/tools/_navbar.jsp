<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-landmark me-2"></i>MicroBank
        </a>
        <ul class="navbar-nav me-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/clients">
                    <i class="fas fa-users me-1"></i>Clients
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/accounts">
                    <i class="fas fa-piggy-bank me-1"></i>Comptes
                </a>
            </li>
            <%-- Lien Utilisateurs visible uniquement pour l'ADMIN --%>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/users">
                        <i class="fas fa-user-cog me-1"></i>Utilisateurs
                    </a>
                </li>
            </c:if>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item">
                <span class="nav-link text-white-50 small">
                    <i class="fas fa-user-circle me-1"></i>
                    <c:out value="${sessionScope.user.prenom}"/>
                    <span class="badge bg-secondary ms-1">
                        <c:out value="${sessionScope.user.role}"/>
                    </span>
                </span>
            </li>
            <li class="nav-item">
                <a class="nav-link text-danger"
                   href="${pageContext.request.contextPath}/auth/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>
                </a>
            </li>
        </ul>
    </div>
</nav>