/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    //在此加载子iFrame 确保其调用主界面myAlert.js时 这个文件已经加载完毕
    //否则可能会丢失分享码获取成功或失败的提示
    $("#mainPageFrame").attr("src", "/page/mainPage.html")
)