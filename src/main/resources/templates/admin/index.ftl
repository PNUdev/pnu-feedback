<#include "include/header.ftl">
<div class="mt-5 p-4">
    <table class="table table-striped">
        <tbody>
        <tr>
            <th scope="row">
                <h3>Відгуків у системі: ${submissionsCount}</h3>
            </th>
            <td></td>
        </tr>
        <tr>
            <th scope="row">
                <p class="h3">Непереглянутих пропозицій: ${unreviewedOpenAnswersCount}</p>
            </th>
            <td>
                <a href="/${adminPanelUrl}/open-answers">
                    <div class="btn btn-secondary mx-4">
                        Переглянути
                    </div>
                </a>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<#include "include/footer.ftl">