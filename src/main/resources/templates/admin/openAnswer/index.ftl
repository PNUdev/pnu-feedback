<#include "../include/header.ftl">

<h2 class="text-center my-3">Відкриті відповіді</h2>

<#if !openAnswersPage?has_content >
    <h3 class="text-center">Список відкритих відповідей пустий</h3>
<#else>
    <table class="table table-striped mx-3">
        <tbody>
        <#list openAnswersPage.getContent() as openAnswer >
            <tr class="container">
                <th scope="row">
                    <div class="block">
                        <p class="px-3">${openAnswer.content}</p>
                    </div>
                    <div class="row">
                        <div class="col">
                        <form class="form-inline float-right" action="/admin/open-answers/approve/${openAnswer.id}" method="post">
                            <div class="d-flex justify-content-center p-1">
                                <button class="btn btn-success btn-sm m-1">Підтвердити</button>
                                <input type="hidden" name="${_csrf.parameterName}"
                                       value="${_csrf.token}"/>
                            </div>
                        </form>
                        </div>
                        <div class="col">
                        <form class="form-inline" action="/admin/open-answers/disapprove/${openAnswer.id}" method="post">
                            <div class="d-flex justify-content-center p-1">
                                <button class="btn btn-danger btn-sm m-1">Відхилити</button>
                                <input type="hidden" name="${_csrf.parameterName}"
                                       value="${_csrf.token}"/>
                            </div>
                        </form>
                        </div>
                    </div>
                </th>
            </tr>
        </#list>
        </tbody>
    </table>
    <div class="row">
        <ul class="pagination mx-auto">
            <#list 1..openAnswersPage.totalPages as pageNumber>
                <form action="/admin/open-answers" method="get">
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
