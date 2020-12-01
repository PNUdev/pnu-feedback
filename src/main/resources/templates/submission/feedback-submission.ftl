<#include "../include/header.ftl">

<form method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <#list scoreQuestions as scoreQuestion>
        <div>
            <div>${scoreQuestion.questionNumber} scoreQuestion.content</div>
            <div>
                <#list 1..5 as score>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio"
                               name="questionNumber-${scoreQuestion.questionNumber}" id="inlineRadio-${score}"
                               value="${score}">
                        <label class="form-check-label" for="inlineRadio-${score}">${score}</label>
                    </div>
                </#list>
            </div>
        </div>
    </#list>
    Що ви хотіли б запропонувати покращити?
    <div class="input-group">
        <textarea class="form-control" aria-label="Відкрите запитання" name="openAnswer"></textarea>
    </div>
    <button class="btn btn-primary">Відправити</button>
</form>

<#include "../include/footer.ftl">

