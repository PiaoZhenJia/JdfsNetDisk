$(
    $("#commonBtn").click(function () {
        parent.changeMainPage("/page/viewFolder.html")
        parent.setPageState("common")
    }),

$("#privateBtn").click(function () {
    parent.changeMainPage("/page/viewFolder.html")
    parent.setPageState("private")
})
)