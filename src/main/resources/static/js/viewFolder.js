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
    /**
     * 新建按钮绑定
     */
    $("#insertNew").click(function () {
        parent.clickFlash($("#insertNew"), 500)
        innerPageInit()
        $("#_inner_page_bg").load("/page/insidePage/insertPage.html #needLoad", function () {
            showFlash($("#needLoad"), 1000)
        })
    }),
    /**
     * 上传按钮绑定
     */
    $("#updateNew").click(async function () {
        clickFlash($("#updateNew"), 500)
        let isLogin = parent.callTopFrameRefresh();
        if (isLogin) {
            parent.myAlert("success", "已确认登录状态")
            if ($("#updateNew").text() == "上传") {
                $("#upLoadFileGiven").click()
            } else {
                parent.myAlert("warning", "同时仅允许一个上传")
            }
        } else {
            parent.myAlert("warning", "上传功能需登陆后使用")
        }

    }),
    /**
     * 选择后自动上传
     */
    $("#upLoadFileGiven").change(function () {
        let formData = new FormData();
        formData.append('file', $('#upLoadFileGiven')[0].files[0]);
        $.ajax({
            url: '/api/file/upload?baseFolder=' + parent.getPageState() + "&uri=" + uri,
            type: 'post',
            data: formData,
            contentType: false,
            processData: false,
            xhr: function () {
                let xhr = new XMLHttpRequest();
                //使用XMLHttpRequest.upload监听上传过程，注册progress事件，打印回调函数中的event事件
                xhr.upload.addEventListener('progress', function (e) {
                    let process = (e.loaded / e.total) * 100;
                    $("#updateNew").text(process.toFixed(0) + "%")
                })
                return xhr;
            },
            success: function (res) {
                $("#updateNew").text("上传")
                parent.myAlert("success", res.message)
                refreshTable()
            },
            error: function (res) {
                $("#updateNew").text("上传")
                if (res.status == 401) {
                    parent.myAlert("error", "登录已经失效")
                } else {
                    parent.myAlert("error", "出错了(" + (res.message != undefined ? res.message : "未知错误") + ")")
                }
            }
        })
        $("#upLoadFileGiven").val("")
    })
)

/**
 * 背景变为深色并提供弹性盒子div
 */
function innerPageInit() {
    let html = "<div id='_inner_page_bg'></div>"
    $("body").append(html)
    showBackground($("#_inner_page_bg"), 500)
}

/**
 * 新建弹窗提交按钮
 */
function submitInsert() {
    if ($("#inputName").val() == '') {
        parent.myAlert("warning", "名称不能为空")
        parent.clickFlash($("#inputName"), 500)
        return
    }
    let choose = $("input[name='fileFolder']:checked").val();
    $.ajax({
        url: "/api/file/create",
        method: "get",
        data: {
            trueFolderFalseFile: choose == 0,
            baseFolder: parent.getPageState(),
            uri: uri,
            name: $("#inputName").val()
        },
        success: function (res) {
            if (res.status != 200) {
                parent.myAlert("error", res.message, 3000)
                return
            }
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
 * 关闭子页面并删除其html
 */
function cancelInnerPage() {
    hideFlash($("#needLoad"), 500)
    hideAndDropBackground($("#_inner_page_bg"), 1000)
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
        url: "/api/file/select/" + parent.getPageState(),
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
                line += "<img src='/ico/delete.png' title='删除文件' onclick='deleteCurrent(\"" + e.name + "\")'>"
                line += "<img src='/ico/edit.png' title='修改名称' onclick='renameCurrent(\"" + e.name + "\")'>"
                // line += "<img src='/ico/download.png' title='高级下载' onclick='downloadCurrent(\"" + e.name + "\")'>"
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
                line += "大小:" + sizeFormatter(e.length)
                line += "</span>"
                line += "</div>"
                //右侧部分
                line += "<div class='tdRightDiv' >"
                line += "<img src='/ico/delete.png' title='删除文件' onclick='deleteCurrent(\"" + e.name + "\")'>"
                line += "<img src='/ico/edit.png' title='修改名称' onclick='renameCurrent(\"" + e.name + "\")'>"
                // line += "<img src='/ico/download.png' title='高级下载' onclick='downloadCurrent(\"" + e.name + "\")'>"
                line += "<img src='/ico/share.png' title='分享文件' onclick='shareCurrent(\"" + e.name + "\")'>"
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
 * 格式化显示文件大小
 * @param numb 文件总字节数
 */
function sizeFormatter(numb) {
    if (numb < 1024) {
        return numb + "B"
    } else {
        numb /= 1024
    }

    if (numb < 1024) {
        return numb.toFixed(2) + "K"
    } else {
        numb /= 1024
    }

    if (numb < 1024) {
        return numb.toFixed(2) + "M"
    } else {
        numb /= 1024
        return numb.toFixed(2) + "G"
    }

}

/**
 * 删除按钮点击事件
 */
function deleteCurrent(name) {
    if (!confirm("确定要删除吗")) {
        return
    }
    $.ajax({
        url: "/api/file/delete",
        data: {
            baseFolder: parent.getPageState(),
            uri: uri + name
        },
        success: function (res) {
            parent.myAlert("success", res.message)
            refreshTable()
        },
        error: function (res) {
            parent.myAlert("error", "请先登录")
        }
    })
}

/**
 * 重命名按钮点击事件
 */
function renameCurrent(name) {
    innerPageInit()
    $("#_inner_page_bg").load("/page/insidePage/renamePage.html #needLoad", function () {
        showFlash($("#needLoad"), 1000)
        $("#oldName").val(name)
        $("#newName").val(name)
        $("#newName").focus()
    })
}

/**
 * 分享文件按钮点击事件
 */
function shareCurrent(name) {//shareId
    $.ajax({
        url: "/api/file/share",
        data: {
            baseFolder: parent.getPageState(),
            uri: uri + name
        },
        success: function (res) {
            if (res.status != 200) {
                parent.myAlert("error", res.message)
                return
            } else {
                innerPageInit()
                //先查看返回的是8位还是12位分享码 如果是12位 后四位作为密码
                let isCommon, shareKey, sharePwd
                if (res.message.length == 8) {
                    isCommon = true
                    shareKey = res.message
                } else {
                    isCommon = false
                    shareKey = res.message.substring(0, 8)
                    sharePwd = res.message.substring(8, 12)
                }
                $("#_inner_page_bg").load("/page/insidePage/sharePage.html #needLoad", function () {
                    showFlash($("#needLoad"), 1000)
                    let text = "我使用朴朴个人云盘分享了: [ " + name + " ]\n"
                    text += "提取链接: [ " + top.location.href + "?share_id=" + shareKey + " ]\n"
                    text += "提取码: [ " + shareKey + " ]\n"
                    text += isCommon ? "无需密码" : ("密码 [ " + sharePwd + " ]")
                    $("#shareName").text("分享文件:" + name)
                    $("#shareId").val(text)
                    parent.myAlert("success", "文件已分享")
                })
            }
        },
        error: function (res) {
            parent.myAlert("error", "请先登录")
        }
    })
}

/**
 * 分享文件复制分享码
 */
function copyShareCode() {
    let code = $("#shareId").val()
    $("#shareId").select()
    if (document.execCommand('copy')) {
        document.execCommand('copy');
        parent.myAlert("success", "已复制内容到剪切板")
    }
}

/**
 * 重命名提交
 */
function submitRename() {
    let oldName = $("#oldName").val()
    let newName = $("#newName").val()
    $.ajax({
        url: "/api/file/rename",
        method: "get",
        data: {
            baseFolder: parent.getPageState(),
            uri: uri,
            oldName: oldName,
            newName: newName
        },
        success: function (res) {
            parent.myAlert("success", res.message)
            refreshTable()
            cancelInnerPage()
        },
        error: function () {
            parent.myAlert("error", "请先登录")
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
