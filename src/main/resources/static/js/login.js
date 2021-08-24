$(
    $("#submitLoginBtn").click(function () {
        $.ajax({
            url: "/api/user/login",
            method: "get",
            data: {
                userName: $("#userName").val(),
                passWord: $("#passWord").val()
            },
            success: function (res) {
                if (res.status == 200) {
                    parent.changeMainPage("/page/desktop.html")
                    parent.callTopFrameRefresh()
                    parent.myAlert("success", res.message)
                } else {
                    parent.myAlert("failed", res.message)
                }
            }
        })
    })
)