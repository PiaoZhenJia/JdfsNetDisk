/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */

//更新头部用户称呼
function getCurrUserName() {
    $("h2").text("欢迎你 " + localStorage.getItem("JDFS_LOGIN_NAME"))
    parent.callTopFrameRefresh()
}

$(
    getCurrUserName(),
    /**
     * 改名按钮单击事件绑定
     */
    $("#changeNameBtn").click(function () {
        clickFlash($("#changeNameBtn"), 500)
    }),
    /**
     * 改密按钮单击事件绑定
     */
    $("#changePwdBtn").click(function () {
        clickFlash($("#changePwdBtn"), 500)
    }),
    /**
     * 用户管理按钮单击事件绑定
     */
    $("#ctrlUserBtn").click(function () {
        clickFlash($("#ctrlUserBtn"), 500)
    }),
    /**
     * 分享管理按钮单击事件绑定
     */
    $("#ctrlShareBtn").click(function () {
        clickFlash($("#ctrlShareBtn"), 500)
    })
)

function changeCtrlPage(uri) {
    $("#ctrlFrame").attr("src", uri)
}