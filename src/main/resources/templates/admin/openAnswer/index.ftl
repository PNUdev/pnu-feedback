<#include "../include/header.ftl">
<h2 class="text-center mt-3">
    Пропозиції
    <h5 class="text-center mb-3">
        <a href="/${adminPanelUrl}/open-answers/reviewed?filter=APPROVED">Підтверджені</a> |
        <a href="/${adminPanelUrl}/open-answers/reviewed?filter=ALL">Всі переглянуті</a> |
        <a href="/${adminPanelUrl}/open-answers/reviewed?filter=DISAPPROVED">Відхилені</a>
    </h5>
</h2>
<#if !openAnswersPage?has_content >
    <h3 class="text-center">Список пропозицій пустий</h3>
<#else>
    <table class="table <table-striped mx-3">
        <tbody>
        <#list openAnswersPage.getContent() as openAnswer >
            <tr class="container">
                <th scope="row">
                    <div class="block">
                        <p class="px-3">${openAnswer.content}</p>
                    </div>
                    <form class="form-inline d-flex justify-content-center" method="post">
                        <div class="d-flex justify-content-center align-middle p-1">
                            <button class="btn btn-success btn-sm m-1"
                                    formaction="/${adminPanelUrl}/open-answers/approve/${openAnswer.id}">
                                Підтвердити
                            </button>
                            <button class="btn btn-danger btn-sm m-1"
                                    formaction="/${adminPanelUrl}/open-answers/disapprove/${openAnswer.id}">
                                Відхилити
                            </button>
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                        </div>
                    </form>
                </th>
            </tr>
        </#list>
        </tbody>
    </table>
    <div class="row">
        <ul class="pagination mx-auto">
            <#list 1..openAnswersPage.totalPages as pageNumber>
                <form action="/${adminPanelUrl}/open-answers" method="get">
                    <li class="page-item">
                        <button type="submit"
                                <#if pageNumber - 1 == openAnswersPage.number>style="background-color: gray" </#if>
                                class="page-link">${pageNumber}
                        </button>
                    </li>
                    <input type="hidden" name="page" value="${pageNumber}">
                </form>
            </#list>
        </ul>
    </div>
</#if>

<#include "../include/footer.ftl">
