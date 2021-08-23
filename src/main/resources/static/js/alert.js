let index = 0

function myAlert(type, msg, time) {
    if (time == undefined || time < 1000) {
        time = 2000
    }
    switch (type) {
        case "success":
            success(msg, time)
            break;
        case "warning":
            warning(msg, time)
            break;
        case "failed":
            failed(msg, time)
            break;
        case "ask":
            return ask(msg, time)
        default:
            alert("unknown alert type !!!!!!!")
    }
}

function success(msg, time) {
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
        "box-shadow: 0 0 1rem white;"+
        "transform-origin: center bottom;"+
        // "z-index: 9999;"+
        "'>"
    html += "<h2>"+ msg +"</h2>"
    html += "</div>"
    $("body").append(html)
    showAlert(randomId)
    setTimeout(function () {
        removeAlert(randomId)
    }, time)
}

function warning(msg, time) {

}

function failed(msg, time) {

}

function ask(msg, time) {
    return undefined;
}

function showAlert(id){
    $("#"+id).css("animation", "showAlert " + 450 + "ms ease-out")
    setTimeout(function () {
        $("#"+id).css("animation", "")
    }, 450);
}

function removeAlert(id){
    $("#"+id).css("animation", "hideAlert " + 450 + "ms ease-out")
    setTimeout(function () {
        $("#"+id).remove()
    }, 450);
}