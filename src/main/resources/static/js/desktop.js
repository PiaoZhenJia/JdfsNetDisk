/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
$(
    /**
     * 公共空间按钮监听
     */
    $("#commonBtn").click(function () {
        parent.changeMainPage("/page/viewFolder.html")
        parent.setPageState("common")
    }),

    /**
     * 私有空间按钮监听
     */
    $("#privateBtn").click(function () {
        parent.changeMainPage("/page/viewFolder.html")
        parent.setPageState("private")
    }),

    /**
     * 分享提取按钮监听
     */
    $("#shareBtn").click(function () {
        parent.changeMainPage("/page/shareDownload.html")
    })
)