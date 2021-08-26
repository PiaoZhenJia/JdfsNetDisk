/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    $("#userName").focus(),
    /**
     * 回车键监听
     */
    $(document).bind('keypress', function (event) {
        if (event.keyCode == "13") {
            event.preventDefault()
            submitLogin()
        }
    })
)

/**
 * 登录方法
 */
function submitLogin() {
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
}