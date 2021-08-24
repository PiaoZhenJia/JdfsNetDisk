/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
/**
 * 此变量用于保存当前页面所在文件路径
 * @type {string} 当前路径
 */
let uri = "/"

$(
    refreshUri(),
    refreshTable(),
    $("#insertNew").click(function () {
        insertInit()
    })
)

/**
 * 新建按钮点击事件:初始化弹窗
 */
function insertInit() {
    let html = "<div id='_insert_alert'></div>"
    $("body").append(html)
    showBackground($("#_insert_alert"), 500)
    $("#_insert_alert").load("/page/insidePage/insertPage.html #needLoad")
}

/**
 * 新建弹窗提交按钮
 */
function submitInsert() {
    let choose = $("input[name='fileFolder']:checked").val();
    $.ajax({
        url: "/create",
        method: "get",
        data: {
            trueFolderFalseFile: choose == 0,
            baseFolder: parent.getPageState(),
            uri: uri,
            name: $("#inputName").val()
        },
        success: function (res) {
            $("#inputName").val("")
            refreshTable()
            parent.myAlert("success", res.message)
        },
        error: function (res) {
            parent.myAlert("error", "请先登录")
        }
    })
}

/**
 * 新建弹窗取消按钮
 */
function cancelInsert() {
    hideAndDropBackground($("#_insert_alert"), 500)
}

/**
 * 重新设置路径并刷新 用于上方路径模块点击事件和文件夹点击事件
 * @param uriNew
 */
function resetUri(uriNew) {
    uri = uriNew
    refreshUri()
    refreshTable()
}

/**
 * 文件夹所在行点击事件:重新设置上方路径、获取文件夹内容并刷新下方内容窗口
 * @param folder
 */
function clickFolder(folder) {
    resetUri(uri + folder + "/")
    refreshUri()
    refreshTable()
}

/**
 * 文件所在行点击事件:触发浏览器直接下载功能
 * @param file
 */
function clickFile(file) {
    window.location.href = "/download/" + parent.getPageState() + "?uri=" + uri + file
}

/**
 * 刷新下方内容窗口
 */
function refreshTable() {
    $.ajax({
        url: "/select/" + parent.getPageState(),
        method: "post",
        contentType: 'application/json',
        data: uri,
        success: function (res) {
            //无权访问时显示
            if (res.status == 401) {
                parent.myAlert("error", res.message)
                parent.callTopFrameRefresh()
                let html = "需登录才能浏览私人路径内容";
                $("#tableDiv").html(html)
            }
            //分别建立文件夹和文件的数组
            let folders = []
            let files = []
            //将获取数据分别塞入数组
            res.dataValue.forEach(e => {
                if (e.isFolder == true) {
                    folders.push(e)
                } else {
                    files.push(e)
                }
            })
            //开始追加html字串
            let html = "";
            let folderIco = '<img src="/ico/folder.png">'
            let fileIco = '<img src="/ico/file.png">'
            //文件夹部分
            folders.forEach(e => {
                //左侧部分
                let line = "<div class='trDiv'>"
                line += "<div class='tdLeftDiv '"
                line += "onclick='clickFolder(\"" + e.name + "\")'"
                line += ">"
                line += folderIco
                line += e.name
                line += "<span>"
                line += e.length + "个文件"
                line += "</span>"
                line += "</div>"
                //右侧部分
                line += "<div class='tdRightDiv' >"
                line += "<img src='/ico/delete.png' title='删除文件'>"
                line += "<img src='/ico/edit.png' title='修改名称'>"
                line += "<img src='/ico/download.png' title='高级下载'>"
                line += "</div>"
                line += "</div>"
                html += line
            })
            //文件部分
            files.forEach(e => {
                //左侧部分
                let line = "<div class='trDiv'>"
                line += "<div class='tdLeftDiv' "
                line += "onclick='clickFile(\"" + e.name + "\")'"
                line += ">"
                line += fileIco
                line += e.name
                line += "<span>"
                line += "大小:" + e.length + "b"
                line += "</span>"
                line += "</div>"
                //右侧部分
                line += "<div class='tdRightDiv' >"
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

/**
 * 通过js获取下方内容窗口应有高度并进行设置
 */
function refreshTableHeight() {
    $("#tableDiv").css("height", "calc(" + ($("#outSideDiv").height() - $("#uriLine").outerHeight()) + "px - 5rem)")
}

/**
 * 刷新上方路径模块
 */
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
    $("#toDesktop").click(function () {
        parent.changeMainPage("/page/desktop.html")
    })
}
