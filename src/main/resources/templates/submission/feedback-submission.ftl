<#include "../include/header.ftl">

<#assign SCORE_QUESTION_PARAM_PREFIX='questionNumber-'/>

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
                <#if educationalProgram??>
                    Освітня програма: ${educationalProgram.title}
                <#else >
                    Опитування ПНУ імені В. Стефаника
                </#if>
            </div>
        </div>
        <form method="POST" id="feedback-submission-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <#if allEducationalPrograms??>
                <div class="my-3 p-3 rounded bg-light">
                    <div class="m-2 p-1 rounded question-content">
                        Виберіть освітню програму
                    </div>
                    <div class="p-2 input-group">
                        <select class="select-program col-md-12" name="educationalProgramId" required>
                            <option value="" disabled selected>Не вибрано</option>
                            <#list allEducationalPrograms as educationalProgram>
                                <option value="${educationalProgram.id}">${educationalProgram.title}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </#if>
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
                                       name="${SCORE_QUESTION_PARAM_PREFIX}${scoreQuestion.questionNumber}"
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

<script>
    $(window).bind("pageshow", function () {
        const form = $('#feedback-submission-form');
        form[0].reset();
    });
</script>

<#if allEducationalPrograms??>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function () {
            $('.select-program').select2();
        });
    </script>
</#if>
<#include "../include/footer.ftl">

