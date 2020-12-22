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
            <th scope="col">Номер запитання</th>
            <th>Запитання</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list scoreQuestions as scoreQuestion >
            <tr>
                <th scope="row" class="px-3">${scoreQuestion.questionNumber}</th>
                <td>${scoreQuestion.content}</td>
                <td>
                    <a href="/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions/edit/${scoreQuestion.id}"
                       role="button"
                       class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
                </td>
            </tr>
        </#list>
    </table>
</#if>

<#include "../include/footer.ftl">
