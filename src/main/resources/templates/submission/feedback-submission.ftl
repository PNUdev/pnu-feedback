<#include "../include/header.ftl">

<style>
    body {
        font-size: 1.2em;
    }
</style>

<div class="col-lg-12">
    <div class="mx-auto my-5 col-lg-9">
        <form method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <#list scoreQuestions as scoreQuestion>
                <div class="my-3 p-3 rounded bg-light">
                    <div class="mb-2 row mx-auto">
                        <div class="mr-2 p-1 rounded border border-secondary font-weight-light">
                            ${scoreQuestion.questionNumber}
                        </div>
                        <div class="my-auto text-wrap text-break">
                            ${scoreQuestion.content}
                        </div>
                    </div>
                    <div>
                        <#list 1..5 as score>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio"
                                       name="questionNumber-${scoreQuestion.questionNumber}"
                                       id="inlineRadio-${score}"
                                       value="${score}" required>
                                <label class="form-check-label" for="inlineRadio-${score}">${score}</label>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
            <div class="my-3 p-3 rounded bg-light">
                <div class="mb-2">
                    Що ви хотіли б запропонувати покращити?
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

