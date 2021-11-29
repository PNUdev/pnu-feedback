<#include "../include/header.ftl">
<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <form method="POST">
        <div class="mb-3">
            <div class="form-check mb-3">
                <input class="form-check-input" name="allowToChooseEducationalProgram" type="checkbox"
                       id="allowToChooseEducationalProgram">
                <label class="form-check-label" for="allowToChooseEducationalProgram">
                    Дозволити вибір освітньої програми при проходженні
                </label>
            </div>
            <div class="input-group mb-3">
                <select class="select-program col-md-12" name="educationalProgramId" id="educationalProgramSelect"
                        required>
                    <option value="" disabled selected>Виберіть освітню програму</option>
                    <#list educationalPrograms as educationalProgram>
                        <option value="${educationalProgram.id}">${educationalProgram.title}</option>
                    </#list>
                </select>
            </div>
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

<#if useLocalAssets == true>
    <link href="/libs/select2-4.1.0-beta1/select2.min.css" rel="stylesheet"/>
    <script src="/libs/select2-4.1.0-beta1/select2.min.js"></script>
<#else>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
</#if>
<script>
    $(document).ready(function () {
        $('.select-program').select2();

        $('#allowToChooseEducationalProgram').change(function () {
            const educationalProgramSelect = $('#educationalProgramSelect')
            if (this.checked) {
                educationalProgramSelect.val('').trigger('change');
                educationalProgramSelect.prop('disabled', true);
                educationalProgramSelect.prop('required', false);
            } else {
                educationalProgramSelect.prop('disabled', false);
                educationalProgramSelect.prop('required', true);
            }
        });
    });
</script>

<#include "../include/footer.ftl">