/**
 * 切换主界面指向
 * @param uri
 */
let pageState = "common"

function changeMainPage(uri){
    hideFlash($("#mainFrame"),500)
    setTimeout(function () {
        $("#mainFrame").attr("src",uri)
        showFlash($("#mainFrame"),500)
    },500)

}

function setPageState(state) {
    pageState = state
}

function getPageState() {
    return pageState;
}

function callTopFrameRefresh() {
    $('#topFrame')[0].contentWindow.refreshLoginStatus()
}