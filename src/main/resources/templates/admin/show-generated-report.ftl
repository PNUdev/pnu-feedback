<#include "include/header.ftl">

<div class="alert alert-success">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Success!</strong> Інформація успішно зібрана та проаналізована!
</div>
<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <form method="post" action="/admin/generate-report/download">
        <div class="mx-auto text-center">
            <button type="submit" class="btn btn-primary">Завантажити згенерований звіт</button>
        </div>
       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <div class="col-md-12 mt-3 d-flex justify-content-center">
        <a href="/admin/generate-report">Назад до генерації звіту</a>
    </div>
</div>

<#include "include/footer.ftl">
