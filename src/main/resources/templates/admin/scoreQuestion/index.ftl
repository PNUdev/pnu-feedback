<#include "../include/header.ftl">

<h2 class="text-center mt-3">Запитання для категорії "${stakeholderCategory.title}"</h2>
<div class="my-3 px-5">
    <a href="/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/new">
        <div class="btn btn-lg btn-primary">Додати питання</div>
    </a>
</div>

<#if !scoreQuestions?has_content >
    <h3 class="text-center">Список запитань пустий</h3>
<#else>
    <table class="table table-striped mx-3">
        <thead>
        <tr>
            <th scope="col">Номер</th>
            <th>Запитання</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list scoreQuestions as scoreQuestion >
            <tr>
                <th scope="row" colspan="3">
                    <div class="row">
                        <div class="col-1">
                            ${scoreQuestion.questionNumber}
                        </div>
                        <div class="col-9">
                            ${scoreQuestion.content}
                        </div>
                        <div class="col-2 align-middle">
                            <a href="/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/edit/${scoreQuestion.id}"
                               role="button"
                               class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
                        </div>
                    </div>
                </th>
            </tr>
        </#list>
    </table>
</#if>

<#include "../include/footer.ftl">
