<#include "include/header.ftl">

<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">

    <div class="input-group">
        <label for="generatedToken" class="h5 mr-4 input-group-text">Згенероване посилання</label>
        <input type="text" value="${generatedTokenLink}" class="form-control" id="generatedTokenLink" readonly>
        <div class="btn btn-light" onclick="copyToClipboard()" id="copy-button">Копіювати</div>
    </div>
    <div class="col-md-12 mt-3 d-flex justify-content-center">
        <a href="/admin/generate-token">Назад до генерації посилання</a>
    </div>

</div>

<script>
    function copyToClipboard() {
        let inputElement = $("#generatedTokenLink");
        inputElement.select();
        inputElement[0].setSelectionRange(0, 99999);
        document.execCommand("copy");

        $("#copy-button").text("Скопійовано!");
    }
</script>

<#include "include/footer.ftl">