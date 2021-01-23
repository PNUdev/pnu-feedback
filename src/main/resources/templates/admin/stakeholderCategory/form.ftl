<#include "../include/header.ftl">
<#assign formSubmissionUrl = stakeholderCategory???then('/${adminPanelUrl}/stakeholder-categories/update/${stakeholderCategory.id}','/${adminPanelUrl}/stakeholder-categories/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Назва</span>
            </div>
            <input type="text" class="form-control" name="title" value="${(stakeholderCategory.title)!}" required>
        </div>

        <div class="p-3">
            <#if stakeholderCategory??>
                <div class="row">
                    <div class="pt-3">
                        <button class="btn btn-primary">Оновити</button>
                    </div>
                    <div class="mx-2 pt-3">
                        <a href="/${adminPanelUrl}/stakeholder-categories/delete/${stakeholderCategory.id}">
                            <div class="btn btn-danger">Видалити</div>
                        </a>
                    </div>
                </div>
            <#else >
                <button class="btn btn-primary">Додати категорію стейкхолдерів</button>
            </#if>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<#include "../include/footer.ftl">
