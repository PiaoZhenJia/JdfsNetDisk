/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
let index = 0

/**
 * 最外层自定义alert实现
 * 显示为屏幕下方半圆形弹框 默认2s延迟后自动消失
 * @param type 共有三种状态 [success 绿色,warning 黄色,error 红色]
 * @param msg 显示的消息字串
 * @param time 单位[ms] 未定义或低于1000会设置为默认值2000 理论动画占用900ms
 */
function myAlert(type, msg, time) {
    if (time != -1 && (time == undefined || time < 1000)) {
        time = 2000
    }
    switch (type) {
        case "success":
            success(msg, time)
            break;
        case "warning":
            warning(msg, time)
            break;
        case "error":
            error(msg, time)
            break;
        default:
            alert("unknown alert type !!!!!!!")
    }
}

/**
 * 抽取的半圆弹窗代码
 * @param msg 消息字串
 * @param time 展示时间
 * @param color 边框颜色
 */
function baseAlert(msg, time, color) {
    let randomId = "alert_" + (++index)
    let html = "<div id='" + randomId + "' style='" +
        "position: absolute;" +
        "width: 100%;" +
        "height: 8%;" +
        "bottom: 0;" +
        "border-radius: 50% 50% 0 0;" +
        "display: flex;" +
        "align-items: center;" +
        "justify-content: center;" +
        "background-color: white;" +
        "box-shadow: 0 0 5rem " + color + ";" +
        "transform-origin: center bottom;" +
        // "z-index: 9999;"+
        "'>"
    html += "<h2>" + msg + "</h2>"
    html += "</div>"
    $("body").append(html)
    showAlert(randomId)
    setTimeout(function () {
        removeAlert(randomId)
    }, time)
}

function success(msg, time) {
    baseAlert(msg, time, "rgba(0,255,0,0.5)")
}

function warning(msg, time) {
    baseAlert(msg, time, "rgba(255,255,0,0.5)")
}

function error(msg, time) {
    baseAlert(msg, time, "rgba(255,0,0,0.5)")
}

/**
 * 显示半圆形弹窗
 * @param id
 */
function showAlert(id) {
    $("#" + id).css("animation", "showAlert " + 450 + "ms ease-out")
    setTimeout(function () {
        $("#" + id).css("animation", "")
    }, 450);
}

/**
 * 删除半圆形弹窗
 * @param id
 */
function removeAlert(id) {
    $("#" + id).css("animation", "hideAlert " + 450 + "ms ease-out")
    setTimeout(function () {
        $("#" + id).hide()
        $("#" + id).remove()
    }, 440);
}