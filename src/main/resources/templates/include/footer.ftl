<script>
    // prevent double form submission
    $('button').click(function () {
        const btn = $(this);
        const form = btn.closest('form')[0];
        if (form && form.checkValidity()) {
            setTimeout(function () {
                btn.prop('disabled', true);
            }, 0);
        }
    });
</script>
</body>
</html>
