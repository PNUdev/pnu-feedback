<#include "../include/header.ftl">

<div class="container">

    <a href="/${adminPanelUrl}/stakeholder-categories/new">
        <div class="btn btn-primary btn-lg btn-block my-4">Додати категорію стейкхолдерів</div>
    </a>

    <#if !stakeholderCategories?has_content >
        <h2 class="text-center">Список категорій стейкхолдерів пустий</h2>
    <#else>
        <table class="table table-striped">
            <tbody>
            <#list stakeholderCategories as stakeholderCategory>
                <tr>
                    <th scope="row">
                        <p class="h4">${stakeholderCategory.title}</p>
                    </th>
                    <td>
                        <a href="/${adminPanelUrl}/stakeholder-categories/${stakeholderCategory.id}/score-questions">
                            <div class="btn btn-secondary mx-4">
                                Переглянути питання
                            </div>
                        </a>
                        <a href="/${adminPanelUrl}/stakeholder-categories/edit/${stakeholderCategory.id}">
                            <div class="btn btn-warning">
                                Редагувати
                            </div>
                        </a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</div>
<#include "../include/footer.ftl">
