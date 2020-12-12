<#include "../include/header.ftl">
<h2 class="text-center mt-3">
    <#if reviewed>
        Відхилені та підтверджені відкриті відповіді
        <h5 class="text-center mb-3"><a href="/admin/open-answers">назад</a></h5>
    <#else>
        Відкриті відповіді<br>
        <h5 class="text-center mb-3"><a href="/admin/open-answers/reviewed">переглянуті</a></h5>
    </#if>
</h2>
<#if !openAnswersPage?has_content >
    <h3 class="text-center">Список відкритих відповідей пустий</h3>
<#else>
    <table class="table <#if reviewed>table-dark<#else>table-striped</#if> mx-3">
        <tbody>
        <#list openAnswersPage.getContent() as openAnswer >
            <tr class="container <#if reviewed && openAnswer.approved>table-secondary</#if>">
                <th scope="row">
                    <div class="block">
                        <p class="px-3 <#if reviewed && openAnswer.approved>text-body</#if>">${openAnswer.content}</p>
                    </div>
                    <div class="row">
                        <#if !reviewed || !openAnswer.approved>
                            <div class="col">
                                <form class="form-inline d-flex justify-content-center"
                                      action="/admin/open-answers/approve/${openAnswer.id}"
                                      method="post">
                                    <div class="d-flex justify-content-center align-middle p-1">
                                        <#if reviewed>
                                            <button class="btn btn-outline-danger btn-sm m-1" disabled>Відхилено</button>
                                        </#if>
                                        <button class="btn btn-success btn-sm m-1">Підтвердити</button>
                                        <input type="hidden" name="${_csrf.parameterName}"
                                               value="${_csrf.token}"/>
                                    </div>
                                </form>
                            </div>
                        </#if>
                        <#if !reviewed || openAnswer.approved>
                            <div class="col">
                                <form class="form-inline d-flex justify-content-center" action="/admin/open-answers/disapprove/${openAnswer.id}"
                                      method="post">
                                    <div class="d-flex justify-content-center p-1">
                                        <#if reviewed>
                                            <button class="btn btn-outline-success btn-sm m-1" disabled>Підтверджено</button>
                                        </#if>
                                        <button class="btn btn-danger btn-sm m-1">Відхилити</button>
                                        <input type="hidden" name="${_csrf.parameterName}"
                                               value="${_csrf.token}"/>
                                    </div>
                                </form>
                            </div>
                        </#if>
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
