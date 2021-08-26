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