/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */

/**
 * 向控制器发送Ajax通过后端解析
 * @param url 直接使用了控制器的拦截路径
 */
function sendToMiscController(url) {
    $.ajax({
        url: "/misc/" + url,
        method: "post",
        data: {
            str: $("#needInput").val()
        },
        success: function (res) {
            $("#needCopy").val(res.dataValue)
        },
        error: function (res) {
            top.myAlert("error","操作失败 请检查输入")
        }
    })
}

/**
 * 复制按钮
 */
$("#copyBtn").click(function () {
    $("#needCopy").select()
    if (document.execCommand('copy')) {
        document.execCommand('copy');
        top.myAlert("success", "已复制内容到剪切板")
    }
})