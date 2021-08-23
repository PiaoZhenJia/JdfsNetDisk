let uri = "/"

$(
    refreshUri(),
    refreshTable(),
    $("#toDesktop").click(function () {
        parent.changeMainPage("/page/desktop.html")
    }),
    $("#insertNew").click(function () {
        insertInit()
    })
)

function insertInit() {
    let html = "<div id='_insert_alert'></div>"
    $("body").append(html)
    showBackground($("#_insert_alert"),500)
    $("#_insert_alert").load("/page/insidePage/insertPage.html #needLoad")
}

function submitInsert(){
    let choose = $("input[name='fileFolder']:checked").val();
    $.ajax({
        url: "/create",
        method: "get",
        data: {
            trueFolderFalseFile: choose == 0,
            baseFolder: parent.getPageState(),
            uri:uri,
            name:$("#inputName").val()
        },
        success: function (res){
            $("#inputName").val("")
            refreshTable()
        }
    })
}
function cancelInsert(){
    hideAndDropBackground($("#_insert_alert"),500)
}

function resetUri(uriNew) {
    uri = uriNew
    refreshUri()
    refreshTable()
}

function clickFolder(folder) {
    resetUri(uri + folder + "/")
    refreshUri()
    refreshTable()
}

function clickFile(file) {
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
                //左侧部分
                if (e.isFolder == true) {//文件夹模式
                    line += "<div class='tdLeftDiv '"
                    line += "onclick='clickFolder(\"" + e.name + "\")'"
                    line += ">"
                    line += folderIco
                    line += e.name
                    line += "<span>"
                    line += "(" + e.length + "个文件)"
                    line += "</span>"
                    line += "</div>"
                } else {//文件模式
                    line += "<div class='tdLeftDiv' "
                    line += "onclick='clickFile(\"" + e.name + "\")'"
                    line += ">"
                    line += fileIco
                    line += e.name
                    line += "<span>"
                    line += "大小:" + e.length + "b"
                    line += "</span>"
                    line += "</div>"
                }
                //右侧部分
                line += "<div class='tdRightDiv' >"
                if (e.isFolder == true) {//文件夹模式

                } else {//文件模式

                }
                //todo 第二个td
                line += "<img src='/ico/delete.png' title='删除文件'>"
                line += "<img src='/ico/edit.png' title='修改名称'>"
                line += "<img src='/ico/download.png' title='高级下载'>"
                line += "</div>"
                line += "</div>"
                html += line
            })
            $("#tableDiv").html(html)
            refreshTableHeight()
        }
    })
}

function refreshTableHeight() {
    $("#tableDiv").css("height", "calc(" + ($("#outSideDiv").height() - $("#uriLine").outerHeight()) + "px - 5rem)")
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
