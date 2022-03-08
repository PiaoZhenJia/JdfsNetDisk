/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */

$("#submitBtn").click(function () {
    clickFlash($("#submitBtn"),500)
    let res = eval($("#inputArea").val())
    $("#outputArea").val(res)
})