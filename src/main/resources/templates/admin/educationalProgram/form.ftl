<#include "../include/header.ftl">
<#assign formSubmissionUrl = educationalProgram???then('/${adminPanelUrl}/educational-programs/update/${educationalProgram.id}','/${adminPanelUrl}/educational-programs/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Назва</span>
            </div>
            <input type="text" class="form-control" name="title" value="${(educationalProgram.title)!}" required>
        </div>

        <div class="p-3">
            <#if educationalProgram??>
                <div class="row">
                    <div class="pt-3">
                        <button class="btn btn-primary">Оновити</button>
                    </div>
                    <div class="mx-2 pt-3">
                        <a href="/${adminPanelUrl}/educational-programs/delete/${educationalProgram.id}">
                            <div class="btn btn-danger">Видалити</div>
                        </a>
                    </div>
                </div>
            <#else >
                <button class="btn btn-primary">Додати освітню програму</button>
            </#if>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<#include "../include/footer.ftl">
