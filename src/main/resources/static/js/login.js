/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    /**
     * 登录按钮监听
     */
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
                    parent.myAlert("error", res.message)
                }
            }
        })
    })
)