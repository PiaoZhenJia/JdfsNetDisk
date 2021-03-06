/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
/**
 * viewFolder应指向哪种目录(公有/私有)
 * @type {string} common或private
 */
let pageState = "common"

$(getShareId())

/**
 * 检测地址栏中是否存在分享id
 */
function getShareId() {
    let url = top.location.href
    let index = url.lastIndexOf("?")
    if (index > 0) {
        let param = url.substr(index + 1, url.length)
        let paramSplit = param.split("=")
        if (paramSplit[0] == "share_id" && paramSplit[1].length == 8) {
            myAlert("success", "检测到分享链接")
            changeMainPage("/page/shareDownload.html")
        } else {
            myAlert("warning", "检测到URL中存在无效参数", 3000)
        }
    }
}

/**
 * 修改主界面指向的html路径
 * @param uri html的项目路径
 */
function changeMainPage(uri) {
    hideFlash($("#mainFrame"), 500)
    setTimeout(function () {
        $("#mainFrame").attr("src", uri)
        showFlash($("#mainFrame"), 500)
    }, 500)

}

/**
 * 公有/私有字串获取
 */
function setPageState(state) {
    pageState = state
}

/**
 * 公有/私有字串设置
 */
function getPageState() {
    return pageState;
}

/**
 * 通知上方固定部分刷新登录状态
 */
function callTopFrameRefresh() {
    return $('#topFrame')[0].contentWindow.refreshLoginStatus()
}

/**
 * 传递 myAlert 调用到最外层页面
 */
function myAlert(type, msg, time) {
    parent.myAlert(type, msg, time)
}