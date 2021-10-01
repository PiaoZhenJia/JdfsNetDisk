/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    refreshLoginStatus(),

    /**
     * 绑定登录按钮
     */
    $("#loginBtn").click(function () {
        clickFlash($("#loginBtn"), 500)
        let str = $("#loginBtn").text()
        switch (str) {
            case "登录":
                parent.changeMainPage("/page/login.html")
                $("#loginBtn").text("取消")
                break;
            case "取消":
                parent.changeMainPage("/page/desktop.html")
                $("#loginBtn").text("登录")
                break;
            case "登出":
                $.ajax({
                    url: "/api/user/logout",
                    method: "get",
                    success: function (res) {
                        parent.myAlert("success", res.message)
                    }
                })
                hideFlash($("#adminBtn"), 500)
                $("#loginBtn").text("登录")
                hideFlash($("#userName"), 500)
                localStorage.removeItem("JDFS_LOGIN_NAME")
                break;
        }
    }),
    $("#adminBtn").click(function () {
            clickFlash($("#adminBtn"), 500)
            let str = $("#adminBtn").text()
            switch (str) {
                case "管理":
                    parent.changeMainPage("/page/admin.html")
                    $("#adminBtn").text("返回")
                    break;
                case "返回":
                    parent.changeMainPage("/page/desktop.html")
                    $("#adminBtn").text("管理")
                    break;
            }
        }
    )
)

/**
 * 更新登录状态
 */
function refreshLoginStatus() {
    let isLogin
    $.ajax({
        url: "/api/user/status",
        method: "get",
        async: false,
        success: function (res) {
            if (res.status == 200) {
                $("#adminBtn").show()
                showFlash($("#adminBtn"), 500)
                $("#loginBtn").text("登出")
                $("#userName").text(res.message)
                showFlash($("#userName"), 500)
                localStorage.setItem("JDFS_LOGIN_NAME", res.message)
                isLogin = true
            } else {
                hideFlash($("#adminBtn"), 500)
                $("#loginBtn").text("登录")
                hideFlash($("#userName"), 500)
                localStorage.removeItem("JDFS_LOGIN_NAME")
                isLogin = false
            }
        }
    })
    return isLogin
}