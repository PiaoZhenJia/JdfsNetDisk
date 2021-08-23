let uri = "/"

$(
    refreshUri(),
    refreshTable(),
    $("#toDesktop").click(function () {
        parent.changeMainPage("/page/desktop.html")
    })
)

function resetUri(uriNew) {
    uri = uriNew
    refreshUri()
    refreshTable()
}

function clickFolder(folder){
    resetUri(uri + folder + "/")
    refreshUri()
    refreshTable()
}

function clickFile(file){
    window.location.href = "/download/" + parent.getPageState() + "?uri=" + uri + file
}

function refreshTable() {
    $.ajax({
        url: "/select/" + parent.getPageState(),
        method: "post",
        contentType: 'application/json',
        data: uri,
        success: function (res) {
            let html = "";
            res.dataValue.forEach(e => {
                let line = "<div class='trDiv'>"
                let folderIco = '<img src="/ico/folder.png">'
                let fileIco = '<img src="/ico/file.png">'
                if (e.isFolder == true) {//文件夹模式
                    line += "<div class='tdLeftDiv '"
                    line += "onclick='clickFolder(\"" + e.name + "\")'"
                    line += ">"
                    line += folderIco
                    line += e.name
                    line += "(" + e.length + "个文件)"
                    line += "</div>"
                } else {
                    line += "<div class='tdLeftDiv' "
                    line += "onclick='clickFile(\"" + e.name + "\")'"
                    line += ">"
                    line += fileIco
                    line += e.name
                    line += "大小:" + e.length + "b"
                    line += "</div>"
                }
                //todo 第二个td
                line += "<div class='tdRightDiv' >操作</div>"

                line += "</div>"
                html += line
            })
            $("#tableDiv").html(html)
            refreshTableHeight()
        }
    })
}

function refreshTableHeight(){
    //alert(($("#outSideDiv").height() - $("#uriLine").outerHeight()))
    //$("#tableDiv").css("height",100)
    $("#tableDiv").css("height","calc(" + ($("#outSideDiv").height() - $("#uriLine").outerHeight()) + "px - 5rem)")
}

function refreshUri() {
    let img = '<img src="/ico/right.png">'
    let html = '<div class="btn uriBtn" id="toDesktop">' + '主页' + '</div>'
    html += img
    html += '<div class="btn uriBtn" onclick=\'resetUri("/")\'>' + parent.getPageState() + '</div>'
    let temp = ''
    for (let i = 0; i < uri.length; i++) {
        if (uri.charAt(i) == '/') {
            if (temp != '') {
                html += '<div class="btn uriBtn" onclick=\'resetUri("' + uri.substr(0, (i + 1)) + '")\'>' + temp + '</div>'
                temp = ''
            }
            if (i < uri.length - 2) {
                html += img
            }
        } else {
            temp += uri.charAt(i)
        }
    }
    $("#uriLine").html(html)
}
