<#include "../include/header.ftl">

<div class="container">

    <a href="/${adminPanelUrl}/educational-programs/new">
        <div class="btn btn-primary btn-lg btn-block my-4">Додати освітню програму</div>
    </a>

    <#if !educationalPrograms?has_content >
        <h2 class="text-center">Список освітніх програм пустий</h2>
    <#else>
        <table class="table table-striped">
            <tbody>
            <#list educationalPrograms as educationalProgram>
                <tr>
                    <th scope="row">
                        <p class="h5">${educationalProgram.title}</p>
                    </th>
                    <td>
                        <a href="/${adminPanelUrl}/educational-programs/edit/${educationalProgram.id}">
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
