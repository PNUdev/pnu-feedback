<#include "../include/header.ftl">

<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <form method="POST">
        <div class="input-group mb-3">
            <select class="select-program col-md-12" name="educationalProgramId" required>
                <option value="" disabled selected>Виберіть освітню програму</option>
                <#list educationalPrograms as educationalProgram>
                    <option value="${educationalProgram.id}">${educationalProgram.title}</option>
                </#list>
            </select>
        </div>
        <div class="input-group mb-3">
            <div class="row">
                <div class="col-sm-6">
                        <label class="control-label" for="datepicker-start">Початкова дата:</label>
                        <input type="text" name="startDate" class="form-control" id="datepicker-start" required>
                </div>
                <div class="col-sm-6">
                        <label class="control-label" for="datepicker-end">Кінцева дата:</label>
                        <input type="text" name="endDate" class="form-control" id="datepicker-end" required>
                </div>
            </div>
        </div>

        <div class="my-4">
            <button class="btn btn-primary">Згенерувати звіт</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <#if warningMessage??>
        <div class="alert alert-danger" role="alert" id="dangerAlert">
            ${warningMessage}
        </div>
    </#if>
</div>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<#--datetime-->
<script src="https://unpkg.com/gijgo@1.9.13/js/gijgo.min.js" type="text/javascript"></script>
<link href="https://unpkg.com/gijgo@1.9.13/css/gijgo.min.css" rel="stylesheet" type="text/css" />
<script>
    $(document).ready(function () {
        $('.select-program').select2();
    });

    //datetimepicker
    $('#datepicker-start').datepicker({
        showOtherMonths: true,
        maxDate: function () {
            return $('#datepicker-end').val();
        }
    });
    $('#datepicker-end').datepicker({
        showOtherMonths: true,
        minDate: function () {
            return $('#datepicker-start').val();
        }
    });

    //alert closing
    setTimeout(function () {
        $('#dangerAlert').alert('close');
    }, 5000);
</script>
<#include "../include/footer.ftl">
