/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
function cancelToDesktop() {
    parent.changeMainPage("/page/desktop.html")
}

function changeUtilPage(uri) {
    $("#runFrame").attr("src", uri)
}