<#include "../include/header.ftl">
<div class="w-100">
    <div class="col-md-5 jumbotron mx-auto mt-5 py-3">
        <div class="h5">${message}</div>
        <div class="row d-flex mt-5">
            <a href="${returnBackUrl}" class="mt-2">
                <div class="btn btn-outline-primary mx-3">Ні, відмінити дію</div>
            </a>
            <form method="POST" class="mt-2">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-outline-danger">Так, видалити!</button>
            </form>
        </div>
    </div>
</div>
<#include "../include/footer.ftl">