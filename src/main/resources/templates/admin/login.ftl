<html>
<head>
    <title>Moderator panel</title>
    <#include "../include/coreDependencies.ftl">
</head>
<body>
<div class="w-100 h-100 d-flex align-items-center justify-content-center">
    <div class="col-md-8">
        <form method="POST" action="/login" class="form-signin">
            <div class="form-group">
                <input name="username" type="text" class="form-control m-3" placeholder="Ім'я користувача"
                       autofocus="true"/>
                <input name="password" type="password" class="form-control m-3" placeholder="Пароль"/>
                <div class="form-row justify-content-center">
                    <button class="btn btn-primary p-3 w-50" type="submit">Увійти</button>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</div>
<#include "./include/footer.ftl">