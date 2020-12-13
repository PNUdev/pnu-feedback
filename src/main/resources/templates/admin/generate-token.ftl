<#include "include/header.ftl">
<!-- Reflected in Java code as MagicConstants.ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM -->
<#assign ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM='-33'/>
<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <form method="POST">
        <div class="input-group mb-3">
            <select class="select-program col-md-12" name="educationalProgramId" required>
                <option value="" disabled selected>Виберіть освітню програму</option>
                <option value="${ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM}">Дозволити вибір при проходженні опитування
                </option>
                <#list educationalPrograms as educationalProgram>
                    <option value="${educationalProgram.id}">${educationalProgram.title}</option>
                </#list>
            </select>
        </div>
        <div class="input-group mb-3">
            <select class="select-program col-md-12" name="stakeholderCategoryId" required>
                <option value="" disabled selected>Виберіть категорію стейкхолдерів</option>
                <#list stakeholderCategories as stakeholderCategory>
                    <option value="${stakeholderCategory.id}">${stakeholderCategory.title}</option>
                </#list>
            </select>
        </div>
        <div class="my-4">
            <button class="btn btn-primary">Згенерувати посилання</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

</div>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<script>
    $(document).ready(function () {
        $('.select-program').select2();
    });
</script>

<#include "include/footer.ftl">