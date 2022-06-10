function reset() {

    const oldPassword = $("#oldPassword").val()
    const newPassword = $("#newPassword").val()
    const authenticationId = $("#authenticationId").val()

    const data = {
        oldPassword: oldPassword,
        newPassword: newPassword
    }

    $.ajax({
        type: "POST",
        url: `/api/users/reset_password/${authenticationId}`,
        data: JSON.stringify(data),
        dataType: "application/json; charset=utf-8;",
        success: function(res) {
            console.log(res)
        }
    })
}