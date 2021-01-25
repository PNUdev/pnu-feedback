<#include "../include/header.ftl">
<#assign formSubmissionUrl = scoreQuestion???then('/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/update/${scoreQuestion.id}',
'/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Категорія стейкхолдерів - "<b>${stakeholderCategory.title}</b>"</span>
            </div>
            <input type="hidden" class="form-control" name="stakeholderCategoryId"
                   value="${(stakeholderCategory.id)!}">
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Номер питання</span>
            </div>
            <input type="text" class="form-control" name="questionNumber" placeholder="(1), (1.1), (1.1.1) ..."
                   value="${(scoreQuestion.questionNumber)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Питання</span>
            </div>
            <textarea class="form-control" name="content" required>${(scoreQuestion.content)!}</textarea>
        </div>

        <div class="p-3">
            <#if scoreQuestion??>
                <div class="row">
                    <div class="pt-3">
                        <button class="btn btn-primary">Оновити</button>
                    </div>
                    <div class="mx-2 pt-3">
                        <a href="/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/delete/${scoreQuestion.id}">
                            <div class="btn btn-danger">Видалити</div>
                        </a>
                    </div>
                </div>
            <#else >
                <button class="btn btn-primary">Додати питання</button>
            </#if>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<#include "../include/footer.ftl">
