/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    shareIdToInput()
)

function shareIdToInput() {
    let url = top.location.href
    let index = url.lastIndexOf("?")
    if (index > 0) {
        let param = url.substr(index + 1, url.length)
        let paramSplit = param.split("=")
        if (paramSplit[0] == "share_id" && paramSplit[1].length == 8) {
            $("#shareId").val(top.location.href.substr(url.lastIndexOf("?") + 1, url.length).split("=")[1])
        }
    }
}

function cancelToDesktop() {
    parent.changeMainPage("/page/desktop.html")
}

function getShareFile() {
    $.ajax({
        url: "/api/file/shareCheck",
        method: "get",
        data: {
            shareKey: $("#shareId").val(),
            sharePwd: $("#sharePwd").val()
        },
        success: function (res) {
            if (res.status != 200) {
                parent.myAlert("error", res.message)
                return
            }
            $("#getShareBtn").hide()
            $("#step1").hide()
            $("#midSpace").hide()
            $("#step2").show()
            $("#shareName").text("文件 : [ " + res.message + " ]")
            $("img").click(function () {
                window.location.href = "/api/file/shareDownload?param=" + res.dataValue
            })
            clickFlash($("#outSide"),500)
        }
    })
}