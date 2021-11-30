/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$("#changeBtn").click(function () {
    clickFlash($("#changeBtn"), 500)
    $("#outputImg").attr("src", "data:image/png;base64," + $("#inputArea").val())
})

$("#outputImg").click(function () {
    console.log($("img")[0].src)
})