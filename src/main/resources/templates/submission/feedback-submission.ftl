<#include "../include/header.ftl">

<style>

    .question-content {
        font-size: 1.2em;
    }

    .score-option {
        font-size: 1.2em;
    }

    .educational-program-label {
        font-size: 1.4em;
    }
</style>

<div class="col-lg-12">
    <div class="mx-auto my-5 col-lg-9">
        <div class="p-3 rounded bg-light educational-program-label">
            <div class="text-center text-wrap mx-auto py-1 px-3 rounded border border-secondary">
                Освітня програма: ${educationalProgram.title}
            </div>
        </div>
        <form method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <#list scoreQuestions as scoreQuestion>
                <div class="my-3 p-3 rounded bg-light">
                    <div class="mb-2 row mx-auto">
                        <div class="mr-2 p-1 rounded border border-secondary">
                            ${scoreQuestion.questionNumber}
                        </div>
                        <div class="my-auto text-wrap text-break question-content">
                            ${scoreQuestion.content}
                        </div>
                    </div>
                    <div class="mx-auto mt-2">
                        <#list 1..5 as score>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio"
                                       name="questionNumber-${scoreQuestion.questionNumber}"
                                       id="inlineRadio-${score}"
                                       value="${score}" required>
                                <label class="form-check-label score-option" for="inlineRadio-${score}">${score}</label>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
            <div class="my-3 p-3 rounded bg-light">
                <div class="mb-2 row mx-auto">
                    <div class=" question-content">
                        Що ви хотіли б запропонувати покращити?
                    </div>
                    <div class="font-weight-light my-auto">
                        (необов'язково)
                    </div>
                </div>
                <div class="input-group">
                    <textarea class="form-control" aria-label="Відкрите запитання" name="openAnswer"></textarea>
                </div>
            </div>
            <div class="p-3">
                <button class="btn btn-primary btn-block">Відправити</button>
            </div>
        </form>
    </div>
</div>

<#include "../include/footer.ftl">

