<html>
<head>
    <#include "../../include/coreDependencies.ftl" >
    <title>Moderator panel</title>
</head>
<body>
<nav class="navbar navbar-dark bg-info">
    <div>
        <a class="navbar-brand" href="/admin">Головна</a>
        <a class="navbar-brand" href="/admin/generate-token">Генерація посилання</a>
        <a class="navbar-brand" href="/admin/stakeholder-categories">Категорії стейкхолдерів</a>
        <a class="navbar-brand" href="/admin/educational-programs">Освітні програми</a>
        <a class="navbar-brand" href="/admin/generate-report">Генерація звіту</a>
    </div>
    <div class="d-inline">
        <form method="POST" action="/logout" class="m-0 p-0">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-light">Вийти</button>
        </form>
    </div>
</nav>
