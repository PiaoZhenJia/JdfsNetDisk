/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */

let fileHeader = ""

//摘抄修改的BASE64转换方法
window.addEventListener("dragenter", function (event) {
    event.preventDefault();
}, false);
window.addEventListener("dragover", function (event) {
    event.preventDefault();
}, false);
window.addEventListener("drop", function (event) {
    let reader = new FileReader();
    reader.onload = function (e) {
        let base64 = e.target.result.split(",")

        let base64Head = base64[0]
        let base64Body = base64[1]
        //截取文件头
        if ($("#checkFileHeader").prop("checked") == false) {
            //如果checkbox未被选中
            $("#b64area").html(base64Body)
        } else {
            //如果checkbox被选中
            $("#b64area").html(base64Head + "," + base64Body)
        }
    };
    reader.readAsDataURL(event.dataTransfer.files[0])
    event.preventDefault();

}, false);

$("#copyBtn").click(function () {
    $("#b64area").select()
    if (document.execCommand('copy')) {
        document.execCommand('copy');
        top.myAlert("success", "已复制内容到剪切板")
    }
})