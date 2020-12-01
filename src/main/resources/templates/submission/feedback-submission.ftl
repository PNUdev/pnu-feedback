<#include "../include/header.ftl">

<form method="POST">

    <#list scoreQuestions as scoreQuestion>
        <div>
            <div>${scoreQuestion.questionNumber} scoreQuestion.content</div>
            <div>
                <#list 1..5 as score>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio${score}"
                               value="${score}">
                        <label class="form-check-label" for="inlineRadio${score}">${score}</label>
                    </div>
                </#list>
            </div>
        </div>
    </#list>
    Що ви хотіли б запропонувати покращити?
    <div class="input-group">
        <textarea class="form-control" aria-label="With textarea"></textarea>
    </div>
    <button class="btn btn-primary">Відправити</button>
</form>

<#include "../include/footer.ftl">

