function clickFlash(obj, ms) {
    obj.css("animation", "clickItem " + ms + "ms ease-out")
    setTimeout(function () {
        obj.css('animation', '')
    }, ms);
}

function showFlash(obj, ms) {
    obj.css("animation", "showItem " + ms + "ms ease-out")
    obj.show()
    setTimeout(function () {
        obj.css('animation', '')
    }, ms);
}

function hideFlash(obj, ms) {
    //经测试，在配置和浏览器引擎均较低的机型上，常规写法会出现闪烁，将毫秒数加十可以基本避免显示问题
    obj.css("animation", "hideItem " + (ms + 10) + "ms ease-out")
    setTimeout(function () {
        obj.css('animation', '')
        obj.hide()
    }, ms);
}

function showBackground(obj, ms){
    obj.css("animation", "showBackground " + ms + "ms ease-out")
    obj.show()
    setTimeout(function () {
        obj.css('animation', '')
    }, ms);
}

function hideAndDropBackground(obj, ms){
    obj.css("animation", "hideBackground " + (ms + 10) + "ms ease-out")
    setTimeout(function () {
        obj.remove()
    }, ms);
}